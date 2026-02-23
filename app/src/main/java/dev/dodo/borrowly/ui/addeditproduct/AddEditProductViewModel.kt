package dev.dodo.borrowly.ui.addeditproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditProductViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditProductUiState())
    val uiState: StateFlow<AddEditProductUiState> = _uiState

    fun changeNameProduct(nameProduct: String) {
        _uiState.update {
            it.copy(
                nameProduct = nameProduct,
                nameProductErrorMessage = when {
                    nameProduct.isBlank() -> "Nama barang tidak boleh kosong."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun refreshProduct(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = productRepository.getProductById(id)
            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        id = it.id,
                        image = it.image,
                        nameProduct = it.title,
                        quantity = it.total.toString(),
                        description = it.description,
                        isLoading = false
                    )
                }
                validateForm()
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun changeQuantity(quantity: String) {
        val regex = Regex("^\\d+$")
        _uiState.update {
            it.copy(
                quantity = quantity,
                quantityErrorMessage = when {
                    quantity.isBlank() -> "Jumlah barang tidak boleh kosong."
                    !regex.matches(quantity) -> "Jumlah barang harus berupa angka."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun changeDescription(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                descriptionErrorMessage = when {
                    description.isBlank() -> "Deskripsi barang tidak boleh kosong."
                    else -> null
                }
            )
        }
        validateForm()
    }

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isFormValid = it.nameProductErrorMessage == null && it.nameProduct.isNotBlank()
                        && it.quantityErrorMessage == null && it.quantity.isNotBlank()
                        && it.descriptionErrorMessage == null && it.description.isNotBlank()
            )
        }
    }

    fun addMyProduct(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val result = productRepository.addProduct(
                product = Product(
                    image = _uiState.value.image,
                    title = _uiState.value.nameProduct,
                    total = _uiState.value.quantity.toLong(),
                    description = _uiState.value.description,
                    ownerId = userRepository.getCurrentUserId() ?: "",
                )
            )
            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                // Munculkan pesan error agar kamu tahu Rules-nya masih nolak
                println("DEBUG ERROR: ${error.message}")
            }
            _uiState.update {
                it.copy(isLoading = false)
            }

        }
    }

    fun savChangeProduct(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = productRepository.editProduct(
                product = Product(
                    id = _uiState.value.id,
                    image = _uiState.value.image,
                    title = _uiState.value.nameProduct,
                    total = _uiState.value.quantity.toLong(),
                    description = _uiState.value.description,
                    ownerId = userRepository.getCurrentUserId() ?: "",
                )
            )
            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                // Munculkan pesan error agar kamu tahu Rules-nya masih nolak
                println("DEBUG ERROR: ${error.message}")
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

}