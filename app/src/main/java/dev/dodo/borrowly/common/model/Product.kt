package dev.dodo.borrowly.common.model

import com.google.firebase.Timestamp

data class Product(
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val total: Long = 0,
    val description: String = "",
    val ownerId: String = ""
)