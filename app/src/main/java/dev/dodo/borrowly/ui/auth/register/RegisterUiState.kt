package dev.dodo.borrowly.ui.auth.register


data class RegisterUiState(
    val email: String = "",
    val emailErrorMessage: String? = null,
    val username: String = "",
    val usernameErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordErrorMessage: String? = null,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false
)