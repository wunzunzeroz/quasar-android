package com.quasar.app

import android.app.Application
import androidx.room.Room
import com.quasar.app.channels.data.ChannelRepository
import com.quasar.app.channels.data.ChannelRepositoryImpl
import com.quasar.app.channels.ui.ChannelsViewModel
import com.quasar.app.map.data.AppDatabase
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.CirclesRepositoryImpl
import com.quasar.app.map.data.LocationRepository
import com.quasar.app.map.data.LocationRepositoryImpl
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolygonsRepositoryImpl
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.PolylinesRepositoryImpl
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.SketchRepositoryImpl
import com.quasar.app.map.data.WaypointsRepository
import com.quasar.app.map.data.WaypointsRepositoryImpl
import com.quasar.app.map.services.UserLocationService
import com.quasar.app.map.services.UserLocationServiceImpl
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
        MapViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel {
        ChannelsViewModel(get())
    }

    // Services
    single<UserLocationService> { UserLocationServiceImpl(get(), get()) }

    // Repositories
    single<WaypointsRepository> { WaypointsRepositoryImpl(get()) }
    single<CirclesRepository> { CirclesRepositoryImpl(get()) }
    single<PolylinesRepository> { PolylinesRepositoryImpl(get()) }
    single<PolygonsRepository> { PolygonsRepositoryImpl(get()) }
    single<SketchRepository> { SketchRepositoryImpl() }

    single<ChannelRepository> { ChannelRepositoryImpl() }
    single<LocationRepository> { LocationRepositoryImpl() }

    // DAO
    single { get<AppDatabase>().waypointDao() }
    single { get<AppDatabase>().polylineDao() }
    single { get<AppDatabase>().polygonDao() }
    single { get<AppDatabase>().circleDao() }

    // DB
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app-database").build()
    }
}