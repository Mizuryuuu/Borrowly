package dev.dodo.borrowly.ui.main

import androidx.annotation.DrawableRes
import dev.dodo.borrowly.R

enum class NavItem(
    val title: String,
    @DrawableRes val icon: Int,
) {
    HOME("Beranda", R.drawable.ic_home),
    STATUS("Status", R.drawable.ic_status),
    PROFILE("Profil", R.drawable.ic_profile),
}