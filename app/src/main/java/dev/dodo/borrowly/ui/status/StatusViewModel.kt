package dev.dodo.borrowly.ui.status

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

class StatusViewModel(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val loanRepository: LoanRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(StatusUiState())
    val uiState: StateFlow<StatusUiState> = _uiState

    init {
        refreshLoans(_uiState.value.selectedChip)
    }

    fun refreshLoans(status: Status? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = loanRepository.getLoans(status)

            result.onSuccess { loans ->
                val uiModel = loans.map { loan ->
                    async {
                        val product = productRepository.getProductById(loan.productId).getOrNull()
                        val owner = userRepository.getUserById(loan.ownerId).getOrNull()
                        val borrower = userRepository.getUserById(loan.borrowerId).getOrNull()

                        if (product != null && owner != null && borrower != null) {
                            LoanDetailUiModel(
                                loan = loan,
                                product = ProductUiModel(
                                    product = product,
                                    location = owner.address,
                                    phoneNumber = owner.phoneNumber,
                                    username = owner.username
                                ),
                                owner = owner,
                                borrower = borrower,
                            )
                        } else null
                    }
                }.awaitAll().filterNotNull()

                _uiState.update { it.copy(productQuery = uiModel, isEmpty = uiModel.isEmpty()) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun changeSelectedChip(chip: Status) {
        _uiState.update { it.copy(selectedChip = chip) }
    }


}