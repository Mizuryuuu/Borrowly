package dev.dodo.borrowly.ui.detailstatus

import dev.dodo.borrowly.common.model.LoanDetailUiModel

data class DetailStatusUiState(
    val loanTransaction: LoanDetailUiModel? = null,
    val showCancelDialog: Boolean = false,
    val cancelLoanId: String? = null,
    val isLoading: Boolean = false
)