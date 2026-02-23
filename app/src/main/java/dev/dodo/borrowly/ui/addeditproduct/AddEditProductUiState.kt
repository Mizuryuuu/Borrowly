package dev.dodo.borrowly.ui.addeditproduct

data class AddEditProductUiState(
    val id: String = "",
    val image: String = "",
    val nameProduct: String = "",
    val nameProductErrorMessage: String? = null,
    val quantity: String = "",
    val quantityErrorMessage: String? = null,
    val description: String = "",
    val descriptionErrorMessage: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false
)