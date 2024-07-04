package com.quasar.app.map.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.quasar.app.channels.data.User
import com.quasar.app.channels.models.UserDetails
import com.quasar.app.map.models.Position
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull

interface LocationRepository {
    fun getUserLocations(channels: Flow<List<String>>): Flow<List<UserLocation>>
    suspend fun updateUserLocation(location: Position)
}

class LocationRepositoryImpl : LocationRepository {
    private val db = Firebase.firestore

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserLocations(channels: Flow<List<String>>): Flow<List<UserLocation>> {
        return channels.flatMapLatest { chnls ->
            if (chnls.isEmpty()) {
                flowOf(emptyList())
            } else {
                db.collection("userLocations").whereIn("channelId", chnls).snapshots()
                    .mapNotNull { it.toObjects<UserLocation>() }
            }
        }
    }

    override suspend fun updateUserLocation(location: Position) {
        try {
//            val userDetails = getUserDetails()
//            val userChannelIds = getJoinedChannels(userDetails.userId)
//
//            userChannelIds.forEach { channelId ->
//                val update = FirebaseChannelMember(
//                    userDetails.userId,
//                    channelId,
//                    userDetails.name,
//                    Instant.now().toString(),
//                    GeoPoint(location.latLngDecimal.latitude, location.latLngDecimal.longitude)
//                )
//                db.collection(channelCollection).document(channelId)
//                    .collection(channelMemberCollection)
//                    .document(update.id).set(update).await()
//            }
//            Log.d("LocationRepository", "User location updated successfully.")
        } catch (e: Exception) {
            Log.e("LocationRepository", "Error updating user location: ${e.message}")
        }
    }
}

data class UserLocation(
    val name: String = "",
    val channelName: String = "",
    val timestamp: String = "",
    val position: GeoPoint = GeoPoint(0.0, 0.0)
)

