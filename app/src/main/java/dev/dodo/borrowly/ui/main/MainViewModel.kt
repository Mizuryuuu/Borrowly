package dev.dodo.borrowly.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dodo.borrowly.common.type.Status
import dev.dodo.borrowly.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        onRefreshUser()
    }

    fun onRefreshUser() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            userRepository.getUser().onSuccess { user ->
                val isIncomplete =
                    user?.phoneNumber?.isBlank() == true || user?.address?.isBlank() == true
                _uiState.update {
                    it.copy(
                        user = user,
                        showPopup = isIncomplete
                    )
                }
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun openStatusWithFilter(filter: Status?) {
        _uiState.update {
            it.copy(
                selectedNavItem = NavItem.STATUS,
                statusFilter = filter
            )
        }
    }

    fun changeSelectedTab(navItem: NavItem) {
        _uiState.update {
            it.copy(selectedNavItem = navItem)
        }
    }

}