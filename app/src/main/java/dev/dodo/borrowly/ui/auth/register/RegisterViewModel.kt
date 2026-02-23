package dev.dodo.borrowly.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun changeEmail(email: String){
        _uiState.update {
            it.copy(
                email = email,
                emailErrorMessage = when {
                    email.isBlank() -> "Email tidak boleh kosong."
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun changeUsername(username: String){
        _uiState.update {
            it.copy(
                username = username,
                usernameErrorMessage = when {
                    username.isBlank() -> "Username tidak boleh kosong."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun changePassword(password: String){
        _uiState.update {
            it.copy(
                password = password,
                passwordErrorMessage = if (password.isBlank()) "Password tidak boleh kosong."
                else if (password.length < 8) "Password minimal 8 karakter."
                else null
            )
        }
        validateForm()
    }

    fun changeConfirmPassword(confirmPassword: String){
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordErrorMessage = if (confirmPassword.isBlank()) "Konfirmasi password tidak boleh kosong."
                else if (confirmPassword.length < 8) "Konfirmasi password minimal 8 karakter."
                else if (confirmPassword != uiState.value.password) "Konfirmasi password tidak sama dengan password."
                else null
            )
        }
        validateForm()
    }

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isFormValid =
                    it.usernameErrorMessage == null &&
                            it.emailErrorMessage == null &&
                            it.passwordErrorMessage == null &&
                            it.confirmPasswordErrorMessage == null
            )
        }
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.register(
                username = uiState.value.username,
                email = uiState.value.email,
                password = uiState.value.password,
            ).onSuccess {
                println("DEBUG: API Success!") // Tambah log ini
                onSuccess()
            }.onFailure { error ->
                println("DEBUG: API Failure! Error: ${error.message}") // Tambah log ini
                _uiState.update {
                    it.copy(errorMessage = error.message ?: "Terjadi kesalahan tidak dikenal")
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}