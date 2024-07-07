package com.quasar.app.map.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.quasar.app.channels.data.ChannelRepository
import com.quasar.app.map.data.LocationRepository
import com.quasar.app.map.data.UserLocation
import com.quasar.app.map.models.Position
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.Instant

interface UserLocationService {
    val userLocations: Flow<List<UserLocation>>
    suspend fun broadCastUserLocation(location: Position)
}

class UserLocationServiceImpl(
    private val channelRepository: ChannelRepository,
    private val locationRepository: LocationRepository
) : UserLocationService {
    private val loggerTag = "UserLocationService"

    override val userLocations: Flow<List<UserLocation>>
        get() = getUserLocationsImpl()

    override suspend fun broadCastUserLocation(location: Position) {
        Log.d(
            loggerTag,
            "Broadcasting user position at lat/lng <${location.latLngDecimal.latitude}/${location.latLngDecimal.longitude}>"
        )

        val user = getUser()
        val userChannels = channelRepository.getUserChannels(user.uid)

        locationRepository.broadcastUserLocation(user, location, userChannels)

    }

    private fun getUserLocationsImpl(): Flow<List<UserLocation>> {

        val userChannels = channelRepository.getUserChannels(getUser().uid) // This is a flow

        return locationRepository.getUserLocations(userChannels)
    }

    private fun getUser(): FirebaseUser {
        return FirebaseAuth.getInstance().currentUser
            ?: throw Exception("User not logged in") // TODO - Move to a repo

    }
}