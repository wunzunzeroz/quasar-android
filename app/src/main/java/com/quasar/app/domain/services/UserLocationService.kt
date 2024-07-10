package com.quasar.app.domain.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quasar.app.data.ChannelRepository
import com.quasar.app.data.LocationRepository
import com.quasar.app.data.UserRepository
import com.quasar.app.domain.models.UserLocation
import com.quasar.app.map.models.Position
import kotlinx.coroutines.flow.Flow

interface UserLocationService {
    fun getUserLocations(): Flow<List<UserLocation>>
    suspend fun broadCastCurrentUserLocation(location: Position)
}

class UserLocationServiceImpl(
    private val locationRepository: LocationRepository,
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository
) : UserLocationService {
    private val loggerTag = "UserLocationService"

    override fun getUserLocations(): Flow<List<UserLocation>> {
        val userChannels = userRepository.getUserChannels()

        return locationRepository.getUserLocations(userChannels)
    }

    override suspend fun broadCastCurrentUserLocation(location: Position) {
        Log.d(
            loggerTag,
            "Broadcasting user position at lat/lng <${location.latLngDecimal.latitude}/${location.latLngDecimal.longitude}>"
        )

        val user = userRepository.getUser()
        val userChannels = userRepository.getUserChannels()

        locationRepository.broadcastUserLocation(user, location, userChannels)
    }
}