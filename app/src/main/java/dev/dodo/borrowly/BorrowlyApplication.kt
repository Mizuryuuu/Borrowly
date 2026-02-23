package dev.dodo.borrowly

import android.app.Application
import dev.dodo.borrowly.di.firebaseModule
import dev.dodo.borrowly.di.repositoryModule
import dev.dodo.borrowly.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BorrowlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BorrowlyApplication)
            modules(viewModelModule, firebaseModule, repositoryModule)
        }
    }
}