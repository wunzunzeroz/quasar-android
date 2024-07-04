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
import com.quasar.app.channels.models.FirebaseChannelMember
import com.quasar.app.channels.models.UserDetails
import com.quasar.app.map.models.Position
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.time.Instant

interface LocationRepository {
    val channelMemberLocations: Flow<List<ChannelMemberLocation>>
    suspend fun updateUserLocation(location: Position)
}

class LocationRepositoryImpl : LocationRepository {
    private val db = Firebase.firestore
    private val userCollection = "users"
    private val channelCollection = "channels"
    private val channelMemberCollection = "members"

    override val channelMemberLocations: Flow<List<ChannelMemberLocation>>
        get() = getUserLocations(getUserDetails().userId)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getUserLocations(userId: String): Flow<List<ChannelMemberLocation>> {
        return getJoinedChannels(userId).flatMapLatest { userChannels ->
            db.collection("userLocations").whereIn("channelId", userChannels).snapshots()
                .mapNotNull { it.toObjects<ChannelMemberLocation>() }
        }
    }

    private fun getJoinedChannels(userId: String): Flow<List<String>> =
        db.collection("users").document(userId).snapshots()
            .mapNotNull { it.toObject<User>()?.channels ?: emptyList() }

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

    private fun getUserDetails(): UserDetails {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User is not authenticated")

        return UserDetails(user.uid, user.email.toString(), user.displayName.toString())
    }
}

data class ChannelMemberLocation(
    val name: String = "",
    val channelName: String = "",
    val timestamp: String = "",
    val position: GeoPoint = GeoPoint(0.0, 0.0)
)

