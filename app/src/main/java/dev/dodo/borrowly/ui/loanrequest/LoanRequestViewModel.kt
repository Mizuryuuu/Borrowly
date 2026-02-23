package dev.dodo.borrowly.ui.loanrequest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.model.LoanDetailUiModel
import dev.dodo.borrowly.common.model.ProductUiModel
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.data.repository.LoanRepository
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanRequestViewModel(
    private val loanRepository: LoanRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoanRequestUiState())
    val uiState: StateFlow<LoanRequestUiState> = _uiState

    init {
        onRefreshLoans()
    }

    fun onRefreshLoans() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = loanRepository.getLoanRequest()

            result.onSuccess { loans ->
                val uiModel = loans.map { loan ->
                    async {
                        val product = productRepository.getProductById(loan.productId).getOrNull()
                        val borrower = userRepository.getUserById(loan.borrowerId).getOrNull()
                        val owner = userRepository.getUserById(loan.ownerId).getOrNull()

                        if (product != null && borrower != null && owner != null) {
                            LoanDetailUiModel(
                                loan = loan,
                                product = ProductUiModel(
                                    product = product,
                                    location = owner.address,
                                    phoneNumber = owner.phoneNumber,
                                    username = owner.username,
                                ),
                                owner = owner,
                                borrower = borrower
                            )
                        } else null
                    }
                }.awaitAll().filterNotNull()

                _uiState.update {
                    it.copy(loanRequest = uiModel, isEmpty = uiModel.isEmpty())
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onAccept(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            loanRepository.editLoanStatus(id, Status.BORROW)
                .onSuccess {
                    onRefreshLoans()
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onReject() {
        val id = _uiState.value.loanRequestRejectId ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentLoan = _uiState.value.loanRequest
                .find { it.loan?.id == id }

            val productId = currentLoan?.loan?.productId
            val total = currentLoan?.loan?.total?.toInt() ?: 0

            if (productId != null) {
                productRepository.increaseProductStock(productId, total)
                    .onSuccess {
                        loanRepository.editLoanStatus(id, Status.REJECT)
                            .onSuccess {
                                onRefreshLoans()
                                dismissRejectDialog()
                                _uiState.update { it.copy(isLoading = false) }
                            }.onFailure {
                                _uiState.update { it.copy(isLoading = false) }
                            }
                    } .onFailure {
                         Log.e("FIRESTORE_ERROR", "Gagal tambah stock: ${it.message}")
                    }
            }
        }
    }

    fun onConfirmReject(id: String) {
        _uiState.update { it.copy(showRejectDialog = true, loanRequestRejectId = id) }
    }

    fun dismissRejectDialog() {
        _uiState.update { it.copy(showRejectDialog = false, loanRequestRejectId = null) }
    }
}