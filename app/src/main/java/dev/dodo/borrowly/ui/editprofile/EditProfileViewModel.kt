package dev.dodo.borrowly.ui.editprofile

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.data.repository.AuthRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

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

    fun changeUsername(username: String) {
        _uiState.update {
            it.copy(
                username = username,
                usernameErrorMessage = when {
                    username.isBlank() -> "Email tidak boleh kosong."
                    username.length < 3 -> "Username minimal 3 karakter."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun changePhoneNumber(phoneNumber: String) {
        val regex = Regex("^\\d+$")
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                phoneNumberErrorMessage = when {
                    phoneNumber.isBlank() -> "Nomor Hape tidak boleh kosong."
                    !regex.matches(phoneNumber) -> "Format nomor hape tidak valid."
                    else -> null
                }
            )
        }
        validateForm()
    }

    fun changeAddress(address: String) {
        _uiState.update {
            it.copy(
                address = address,
                addressErrorMessage = when {
                    address.isBlank() -> "Alamat tidak boleh kosong."
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

    fun changeNewPassword(newPassword: String) {
        _uiState.update {
            it.copy(
                newPassword = newPassword,
                newPasswordErrorMessage = if (newPassword.length < 8) "Password baru minimal 8 karakter."
                else ""
            )
        }
        validateForm()
    }

    fun refreshUser() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            if (userId != null) {
                userRepository.getUserById(userId).onSuccess { user ->
                    if (user != null) {
                        _uiState.update {
                            it.copy(
                                username = user.username,
                                email = user.email,
                                password = user.password,
                                phoneNumber = user.phoneNumber,
                                address = user.address,
                            )
                        }
                    }
                }.onFailure {
                    Log.d("DEBUG ERROR", it.message.toString())
                }
            }
        }
    }

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isFormValid = it.usernameErrorMessage == null && it.username.isNotBlank()
                        && it.emailErrorMessage == null && it.email.isNotBlank()
                        && it.passwordErrorMessage == null && it.password.isNotBlank()
                        && it.phoneNumberErrorMessage == null && it.phoneNumber.isNotBlank()
                        && it.addressErrorMessage == null && it.address.isNotBlank()
            )
        }
    }

    fun saveProfileChanges(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.updateProfile(
                username = _uiState.value.username,
                email = _uiState.value.email,
                password = _uiState.value.password,
                phoneNumber = _uiState.value.phoneNumber,
                address = _uiState.value.address,
                newPassword = _uiState.value.newPassword
            ).onSuccess {
                onSuccess()
            }.onFailure {
                // Munculkan pesan error agar kamu tahu Rules-nya masih nolak
                println("DEBUG ERROR: ${it.message}")
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}