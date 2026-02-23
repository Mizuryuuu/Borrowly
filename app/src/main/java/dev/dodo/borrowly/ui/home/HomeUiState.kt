package dev.dodo.borrowly.ui.home

import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User

data class HomeUiState(
    val user: User? = null,
    val product: List<ProductUiModel> = listOf(),
    val isEmpty: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
)