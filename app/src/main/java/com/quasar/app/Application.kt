package com.quasar.app

import android.app.Application
import com.quasar.app.map.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Quasar : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Quasar)
            modules(appModule)
        }
    }
}

val appModule = module {
    // ViewModels
    viewModel {
        MapViewModel()
    }

    // Repositories

    // DB
}