package dev.dodo.borrowly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dev.dodo.borrowly.common.model.Product
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereNotEqualTo("ownerId", auth.currentUser?.uid)
                .get()
                .await()
            val product = snapshot.documents.mapNotNull {
                it.toObject(Product::class.java)?.copy(id = it.id) }
                .filter { it.ownerId != auth.currentUser?.uid && (it.total) >= 1
            }

            println(snapshot.size())
            println(auth.currentUser?.uid)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: String): Result<Product> {
        return try {
            val document = firestore.collection("products")
                .document(id)
                .get()
                .await()
            val product = document.toObject(Product::class.java)?.copy(
                id = document.id
            )
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyProducts(): Result<List<Product>> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            val snapshot = firestore.collection("products")
                .whereEqualTo("ownerId", user.uid)
                .get()
                .await()
            val product = snapshot.documents.mapNotNull { document ->
                document.toObject(Product::class.java)?.copy(
                    id = document.id
                )
            }
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(id)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            val productDetail = product.copy(ownerId = user.uid)
            val document = firestore.collection("products")
                .add(productDetail)
                .await()
            document.update("id", document.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .get()
                .await()
            val products = snapshot.documents.mapNotNull { document ->
                document.toObject(Product::class.java)?.copy(id = document.id)
            }.filter { product ->
                val matchesQuery = product.title.contains(query, ignoreCase = true)
                val notOwner = product.ownerId != auth.currentUser?.uid
                val hasStock = (product.total) >= 1

                matchesQuery && notOwner && hasStock
            }

            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editProduct(product: Product): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            firestore.collection("products")
                .document(product.id)
                .update(
                    mapOf(
                        "title" to product.title,
                        "description" to product.description,
                        "total" to product.total,
                        "image" to product.image,
                        "ownerId" to user.uid,
                    )
                ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reduceProductStock(productId: String, amount: Int): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(productId)
                .update("total", FieldValue.increment(-amount.toLong()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun increaseProductStock(productId: String, amount: Int): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(productId)
                .update("total", FieldValue.increment(amount.toLong()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}