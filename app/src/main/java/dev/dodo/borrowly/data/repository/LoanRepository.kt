package dev.dodo.borrowly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.type.Status
import kotlinx.coroutines.tasks.await

class LoanRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun getLoans(status: Status? = null): Result<List<Loan>> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            var query = firestore.collection("loans")
                .whereEqualTo("borrowerId", user.uid)
            if (status != null && status != Status.ALL) {
                query = query.whereEqualTo("status", status.name)
            }
            val snapshot = query.get().await()

            val loans = snapshot.documents.mapNotNull { document ->
                document.toObject(Loan::class.java)?.copy(
                    id = document.id
                )
            }
            Result.success(loans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLoanById(id: String): Result<Loan> {
        return try {
            val document = firestore.collection("loans")
                .document(id)
                .get()
                .await()
            val loan = document.toObject(Loan::class.java)?.copy(
                id = document.id
            )
            if (loan != null) {
                Result.success(loan)
            } else {
                Result.failure(Exception("Loan not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLoanRequest(): Result<List<Loan>> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            val document = firestore.collection("loans")
                .whereEqualTo("ownerId", user.uid)
                .whereEqualTo("status", Status.PROSES.name)
                .get()
                .await()
            val loans = document.documents.mapNotNull { document ->
                document.toObject(Loan::class.java)?.copy(
                    id = document.id
                )
            }
            Result.success(loans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasLoanRequest(): Result<Boolean> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))

            val snapshot = firestore.collection("loans")
                .whereEqualTo("ownerId", user.uid)
                .whereEqualTo("status", Status.PROSES.name)
                .limit(1)
                .get()
                .await()

            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addLoan(loan: Loan): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("User not logged in"))
            val loanDetail = loan.copy(borrowerId = user.uid)
            val document = firestore.collection("loans")
                .add(loanDetail)
                .await()
            document.update("id", document.id).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editLoanStatus(id: String, status: Status): Result<Unit> {
        return try {
            firestore.collection("loans")
                .document(id)
                .update("status", status.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteLoan(id: String): Result<Unit> {
        return try {
            firestore.collection("loans")
                .document(id)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}