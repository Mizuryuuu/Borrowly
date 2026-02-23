package dev.dodo.borrowly.ui.myproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProductViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyProductUiState())
    val uiState: StateFlow<MyProductUiState> = _uiState

    init {
        refreshMyProduct()
    }

    fun refreshMyProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            productRepository.getMyProducts().onSuccess { products ->
                val uiModels = products.map { product ->
                    async {
                        userRepository.getUserById(product.ownerId).getOrNull()?.let { user ->
                            ProductUiModel(
                                product = product,
                                location = user.address,
                                phoneNumber = user.phoneNumber,
                                username = user.username
                            )
                        }
                    }
                }.awaitAll().filterNotNull()
                _uiState.update { it.copy(product = uiModels) }
            }.onFailure {
                Log.d(
                    "MyProductViewModel",
                    "refreshMyProduct: ${it.message}"
                )
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onConfirmDelete(id: String) {
        _uiState.update { it.copy(showDeleteDialog = true, productIdToDelete = id) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, productIdToDelete = null) }
    }

    fun deleteProduct() {
        val id = _uiState.value.productIdToDelete ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            productRepository.deleteProduct(id).onSuccess {
                dismissDeleteDialog()
                refreshMyProduct()
            }.onFailure {
                dismissDeleteDialog()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


}