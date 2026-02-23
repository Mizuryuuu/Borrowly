package dev.dodo.borrowly.di

import dev.dodo.borrowly.data.repository.AuthRepository
import dev.dodo.borrowly.data.repository.LoanRepository
import dev.dodo.borrowly.data.repository.ProductRepository
import dev.dodo.borrowly.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get(), get()) }
    single { UserRepository(get(), get()) }
    single { ProductRepository(get(), get()) }
    single { LoanRepository(get(), get()) }
}