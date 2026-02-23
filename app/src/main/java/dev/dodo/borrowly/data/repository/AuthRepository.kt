package dev.dodo.borrowly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.dodo.borrowly.common.model.User
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun register(username: String, email: String, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                val user = User(
                    username = username,
                    email = email,
                    password = password,
                )
                firestore.collection("users")
                    .document(it.uid)
                    .set(user)
                    .await()
            }
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
    suspend fun updateProfile(username: String, email: String, password: String, phoneNumber: String, address: String, newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            firestore.collection("users")
                .document(user.uid)
                .update(
                    mapOf(
                        "username" to username,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "address" to address,
                        "password" to password,
                    )
                ).await()
            if (user.email != email) {
                user.updateEmail(email).await()
            }

            if (password.isNotEmpty() && newPassword.isNotEmpty() && password != newPassword) {
                user.updatePassword(newPassword).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}