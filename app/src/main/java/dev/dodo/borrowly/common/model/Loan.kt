package dev.dodo.borrowly.common.model

import com.google.firebase.Timestamp
import dev.dodo.borrowly.common.type.Status

data class Loan(
    val id: String = "",
    val status: Status = Status.PROSES,
    val ownerId: String = "",
    val productId: String = "",
    val borrowerId: String = "",
    val total: String = "",
    val createdAt: Timestamp? = null
)