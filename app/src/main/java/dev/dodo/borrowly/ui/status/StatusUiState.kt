package dev.dodo.borrowly.ui.status

import dev.dodo.borrowly.common.model.Loan
import dev.dodo.borrowly.common.model.LoanDetailUiModel
import dev.dodo.borrowly.common.type.Status

data class StatusUiState(
    val productQuery: List<LoanDetailUiModel> = listOf(),
    val selectedChip: Status = Status.ALL,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false
)