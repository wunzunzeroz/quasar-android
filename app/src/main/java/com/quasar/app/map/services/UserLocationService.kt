package com.quasar.app.map.services

import com.google.firebase.auth.FirebaseAuth
import com.quasar.app.channels.data.ChannelRepository
import com.quasar.app.map.data.LocationRepository
import com.quasar.app.map.data.UserLocation
import kotlinx.coroutines.flow.Flow

interface UserLocationService {
    val userLocations: Flow<List<UserLocation>>
}

class UserLocationServiceImpl(
    private val channelRepository: ChannelRepository,
    private val locationRepository: LocationRepository
) : UserLocationService {
    override val userLocations: Flow<List<UserLocation>>
        get() = getUserLocationsImpl()

    private fun getUserLocationsImpl(): Flow<List<UserLocation>> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("User not logged in") // TODO - Move to a repo

        val userChannels = channelRepository.getUserChannels(userId) // This is a flow

        return locationRepository.getUserLocations(userChannels)
    }
}