package dev.dodo.borrowly.ui.detailproduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.data.repository.LoanRepository
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.String

class DetailProductViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val loanRepository: LoanRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailProductUiState())
    val uiState: StateFlow<DetailProductUiState> = _uiState.asStateFlow()

    fun showPopup() {
        _uiState.update {
            it.copy(showPopup = true)
        }
    }

    fun hidePopup() {
        _uiState.update {
            it.copy(showPopup = false)
        }
    }

    fun decrementTotal() {
        _uiState.update { currentState ->
            val currentTotal = currentState.total.toIntOrNull() ?: 1
            val newTotal = (currentTotal - 1).coerceAtLeast(1)
            currentState.copy(total = newTotal.toString())
        }
    }

    fun incrementTotal() {
        _uiState.update { currentState ->
            val currentTotal = currentState.total.toIntOrNull() ?: 1
            val maxStock: Long = currentState.product?.product?.total ?: 0

            val newTotal = if (currentTotal < maxStock) currentTotal + 1 else currentTotal
            currentState.copy(total = newTotal.toString())
        }
    }

    fun onProductRefresh(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            productRepository.getProductById(id).onSuccess { product ->
                userRepository.getUserById(product.ownerId).onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            product = user?.let { data ->
                                ProductUiModel(
                                    product = product,
                                    username = data.username,
                                    location = data.address,
                                    phoneNumber = data.phoneNumber
                                )
                            },
                            total = if ((product.total) > 0) "1" else "0",
                            isLoading = false
                        )
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun loanProduct(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentUserId = userRepository.getCurrentUserId() ?: ""
            val productData = uiState.value.product?.product
            val amountToLoan = uiState.value.total.toIntOrNull() ?: 0

            if (productData == null || currentUserId.isEmpty() || amountToLoan <= 0) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            loanRepository.addLoan(
                loan = Loan(
                    ownerId = productData.ownerId,
                    productId = productData.id,
                    borrowerId = currentUserId,
                    total = amountToLoan.toString(),
                    status = Status.PROSES,
                    createdAt = Timestamp.now()
                )
            ).onSuccess {
                productRepository.reduceProductStock(productData.id, amountToLoan).onSuccess {
                    _uiState.update { it.copy(isLoading = false, showPopup = false) }
                    onSuccess()
                }
            }.onFailure { error ->
                Log.e("FIRESTORE_ERROR", "Gagal tambah loan: ${error.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}