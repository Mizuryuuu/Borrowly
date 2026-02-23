package dev.dodo.borrowly.di

import dev.dodo.borrowly.ui.addeditproduct.AddEditProductViewModel
import dev.dodo.borrowly.ui.auth.login.LoginViewModel
import dev.dodo.borrowly.ui.auth.register.RegisterViewModel
import dev.dodo.borrowly.ui.detailproduct.DetailProductViewModel
import dev.dodo.borrowly.ui.detailstatus.DetailStatusViewModel
import dev.dodo.borrowly.ui.editprofile.EditProfileViewModel
import dev.dodo.borrowly.ui.home.HomeViewModel
import dev.dodo.borrowly.ui.loanrequest.LoanRequestViewModel
import dev.dodo.borrowly.ui.main.MainViewModel
import dev.dodo.borrowly.ui.myproduct.MyProductViewModel
import dev.dodo.borrowly.ui.profile.ProfileViewModel
import dev.dodo.borrowly.ui.status.StatusViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { StatusViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { DetailProductViewModel(get(), get(), get()) }
    viewModel { DetailStatusViewModel(get(), get(), get()) }
    viewModel { LoanRequestViewModel(get(), get(), get()) }
    viewModel { EditProfileViewModel(get(),get()) }
    viewModel { MyProductViewModel(get(), get()) }
    viewModel { AddEditProductViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}