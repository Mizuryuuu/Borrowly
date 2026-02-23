package dev.dodo.borrowly.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.data.repository.AuthRepository
import dev.dodo.borrowly.data.repository.LoanRepository
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val loanRepository: LoanRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        refreshUser()
        checkLoanRequest()
    }

    fun refreshUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            userRepository.getUser().onSuccess { user ->
                _uiState.update {
                    it.copy(user = user)
                }
            }
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun checkLoanRequest(){
        viewModelScope.launch {
            loanRepository.hasLoanRequest().onSuccess { hasLoan ->
                _uiState.update {
                    it.copy(hasLoanRequest = hasLoan)
                }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()
        onSuccess()
    }
}