package com.quasar.app

import android.app.Application
import androidx.room.Room
import com.quasar.app.map.data.AppDatabase
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.CirclesRepositoryImpl
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolygonsRepositoryImpl
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.PolylinesRepositoryImpl
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.SketchRepositoryImpl
import com.quasar.app.map.data.WaypointsRepository
import com.quasar.app.map.data.WaypointsRepositoryImpl
import com.quasar.app.map.ui.MapViewModel
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
        MapViewModel(get(), get(), get(), get(), get())
    }

    // Repositories
    single<WaypointsRepository> { WaypointsRepositoryImpl(get()) }
    single<CirclesRepository> { CirclesRepositoryImpl() }
    single<PolylinesRepository> { PolylinesRepositoryImpl() }
    single<PolygonsRepository> { PolygonsRepositoryImpl() }
    single<SketchRepository> { SketchRepositoryImpl() }

    // DAO
    single { get<AppDatabase>().waypointDao() }

    // DB
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app-database").build()
    }
}