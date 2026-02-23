package dev.dodo.borrowly.ui.auth.login

data class LoginUiState (
    val email: String = "",
    val emailErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false
)