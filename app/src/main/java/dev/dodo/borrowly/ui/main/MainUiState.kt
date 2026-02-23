package dev.dodo.borrowly.ui.main

import dev.dodo.borrowly.common.model.User
import dev.dodo.borrowly.common.type.Status

data class MainUiState(
    val selectedNavItem: NavItem = NavItem.HOME,
    val user: User? = null,
    val statusFilter: Status? = null,
    val isLoading: Boolean = false,
    val showPopup: Boolean = false
)