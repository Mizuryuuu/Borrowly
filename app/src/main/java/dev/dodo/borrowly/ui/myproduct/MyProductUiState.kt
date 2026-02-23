package dev.dodo.borrowly.ui.myproduct

import dev.dodo.borrowly.common.model.ProductUiModel

data class MyProductUiState(
    val product: List<ProductUiModel> = listOf(),
    val showDeleteDialog: Boolean = false,
    val productIdToDelete: String? = null,
    val isLoading: Boolean = false,
)