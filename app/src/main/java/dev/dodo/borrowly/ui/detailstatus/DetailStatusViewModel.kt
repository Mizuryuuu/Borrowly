package dev.dodo.borrowly.ui.detailstatus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.model.LoanDetailUiModel
import dev.dodo.borrowly.common.model.Product
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.data.repository.LoanRepository
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailStatusViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val loanRepository: LoanRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailStatusUiState())
    val uiState: StateFlow<DetailStatusUiState> = _uiState.asStateFlow()

    fun onLoanRefresh(loanId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            loanRepository.getLoanById(loanId).onSuccess { loan ->
                val productDeferred =
                    async { productRepository.getProductById(loan.productId).getOrNull() }
                val borrowerDeferred =
                    async { userRepository.getUserById(loan.borrowerId).getOrNull() }

                val product = productDeferred.await()
                val borrower = borrowerDeferred.await()

                if (product != null) {
                    userRepository.getUserById(product.ownerId).onSuccess { owner ->
                        _uiState.update {
                            it.copy(
                                loanTransaction = LoanDetailUiModel(
                                    loan = loan,
                                    product = ProductUiModel(
                                        product = Product(
                                            total = loan.total.toLong(),
                                            image = product.image,
                                            title = product.title,
                                            description = product.description,
                                            id = product.id
                                        ),
                                        username = owner?.username ?: "",
                                        location = owner?.address ?: "",
                                        phoneNumber = owner?.phoneNumber ?: ""
                                    ),
                                    borrower = borrower,
                                    owner = owner
                                ),
                                isLoading = false
                            )
                        }
                    }.onFailure { _uiState.update { it.copy(isLoading = false) } }
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDoneLoan(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentLoan = _uiState.value.loanTransaction

            if (currentLoan?.loan?.id != id) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val productId = currentLoan.loan.productId
            val total = currentLoan.loan.total.toInt()

            try {
                loanRepository.editLoanStatus(id, Status.FINISH).getOrThrow()
                productRepository.increaseProductStock(productId, total).getOrThrow()

                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun onCancelLoan(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentLoan = _uiState.value.loanTransaction
            if (currentLoan?.loan?.id != id) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val productId = currentLoan.loan.productId
            val total = currentLoan.loan.total.toInt()

            try {
                loanRepository
                    .deleteLoan(id)
                    .getOrThrow()

                productRepository
                    .increaseProductStock(productId, total)
                    .getOrThrow()

                Log.d("CANCEL_LOAN", "Success delete $id")

                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                Log.e("CANCEL_LOAN", "Error: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    }

    fun onConfirmReject(id: String) {
        _uiState.update { it.copy(showCancelDialog = true, cancelLoanId = id) }
    }

    fun dismissRejectDialog() {
        _uiState.update { it.copy(showCancelDialog = false, cancelLoanId = null) }
    }

}