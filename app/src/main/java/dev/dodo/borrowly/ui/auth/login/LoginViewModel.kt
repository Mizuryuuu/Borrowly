package dev.dodo.borrowly.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.data.repository.AuthRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun changeEmail(email: String) {
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

    fun changePassword(password: String) {
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

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isFormValid = it.emailErrorMessage == null && it.email.isNotBlank()
                        && it.passwordErrorMessage == null && it.password.isNotBlank()
            )
        }
    }

    fun login(
        onSuccessWithFilledPreferences: () -> Unit,
        onSuccessWithUnfilledPreferences: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            authRepository.login(
                email = uiState.value.email,
                password = uiState.value.password,
            ).onSuccess {
                userRepository.getUser()
                    .onSuccess { user ->
                        if (user?.preferences?.isNotEmpty() == true) {
                            onSuccessWithFilledPreferences()
                        } else {
                            onSuccessWithUnfilledPreferences()
                        }
                    }
                    .onFailure {
                        onSuccessWithUnfilledPreferences()
                    }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}