package dev.dodo.borrowly.ui.profile

import dev.dodo.borrowly.common.model.User

data class ProfileUiState(
    val user: User? = null,
    val hasLoanRequest: Boolean = false,
    val isLoading: Boolean = false
)