package dev.dodo.borrowly.common.model

data class ProductUiModel(
    val product: Product? = null,
    val location: String = "",
    val phoneNumber: String = "",
    val username: String = "",
)