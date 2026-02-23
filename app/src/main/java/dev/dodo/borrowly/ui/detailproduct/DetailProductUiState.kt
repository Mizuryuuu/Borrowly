package dev.dodo.borrowly.ui.detailproduct

import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.model.User

data class DetailProductUiState (
    val product: ProductUiModel? = null,
    val showPopup: Boolean = false,
    val total: String = "1",
    val isLoading: Boolean = false
)