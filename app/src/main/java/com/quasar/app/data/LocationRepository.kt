package com.quasar.app.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.quasar.app.domain.models.UserLocation
import com.quasar.app.map.models.Position
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.time.Instant

interface LocationRepository {
    fun getUserLocations(channels: Flow<List<String>>): Flow<List<UserLocation>>
    suspend fun broadcastUserLocation(
        userId: FirebaseUser, location: Position, userChannels: Flow<List<String>>
    )
}

class LocationRepositoryImpl : LocationRepository {
    private val db = Firebase.firestore

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserLocations(channels: Flow<List<String>>): Flow<List<UserLocation>> {
        return channels.flatMapLatest { channelIds ->
            if (channelIds.isEmpty()) {
                flowOf(emptyList())
            } else {
                db.collection("userLocations").whereArrayContainsAny("channelIds", channelIds)
                    .snapshots()
                    .mapNotNull { it.toObjects<UserLocation>() }
            }
        }
    }

    override suspend fun broadcastUserLocation(
        user: FirebaseUser, location: Position, userChannels: Flow<List<String>>
    ) {
        userChannels.collectLatest { channelIds ->
            val locationUpdate = UserLocation(
                userId = user.uid,
                userName = user.displayName.toString(),
                timestamp = Instant.now().toString(),
                channelIds = channelIds,
                position = GeoPoint(
                    location.latLngDecimal.latitude, location.latLngDecimal.longitude
                )
            )
            db.collection("userLocations").document(user.uid).set(locationUpdate).await()
        }
    }
}

