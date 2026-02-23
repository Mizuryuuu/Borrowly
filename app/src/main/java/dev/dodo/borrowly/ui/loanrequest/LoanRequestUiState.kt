package dev.dodo.borrowly.ui.loanrequest

import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.model.LoanDetailUiModel


data class LoanRequestUiState(
    val loanRequest: List<LoanDetailUiModel> = listOf(),
    val isEmpty: Boolean = false,
    val showRejectDialog: Boolean = false,
    val loanRequestRejectId: String? = null,
    val isLoading: Boolean = false,
)