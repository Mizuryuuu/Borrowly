package dev.dodo.borrowly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dev.dodo.borrowly.common.model.User
import kotlinx.coroutines.tasks.await

class UserRepository (
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun getUser(): Result<User?> {
        try {
            val user = auth.currentUser
            if (user != null) {
                val userSnapshot = firestore.collection("users")
                    .document(user.uid)
                    .get()
                    .await()
                if (userSnapshot.exists()) {
                    return Result.success(userSnapshot.toObject(User::class.java))
                }
            }
            return Result.success(null)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getUserById(id: String): Result<User?> {
        try {
            val userSnapshot = firestore.collection("users")
                .document(id)
                .get()
                .await()
            if (userSnapshot.exists()) {
                return Result.success(userSnapshot.toObject(User::class.java))
            }
            return Result.success(null)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun savePreferences(preferences: Map<String, List<Int>>): Result<Unit> {
        return try {
            val user = auth.currentUser
            if (user != null) {
                firestore.collection("users")
                    .document(user.uid)
                    .set(mapOf("preferences" to preferences), SetOptions.merge())
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}