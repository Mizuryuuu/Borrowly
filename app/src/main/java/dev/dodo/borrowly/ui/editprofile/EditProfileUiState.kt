package dev.dodo.borrowly.ui.editprofile

data class EditProfileUiState(
    val username: String = "",
    val usernameErrorMessage: String? = null,
    val email: String = "",
    val emailErrorMessage: String? = null,
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String? = null,
    val address: String = "",
    val addressErrorMessage: String? = null,
    val password: String = "",
    val passwordErrorMessage: String? = null,
    val newPassword: String = "",
    val newPasswordErrorMessage: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false
)