package com.quasar.app.map.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.quasar.app.channels.models.FirebaseChannelMember
import com.quasar.app.channels.models.UserDetails
import com.quasar.app.map.models.Position
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface LocationRepository {
    val channelMemberLocations: Flow<List<ChannelMemberLocation>>
    fun updateUserLocation(location: Position)
}

class LocationRepositoryImpl : LocationRepository {
    private val db = Firebase.firestore
    private val userCollection = "users"
    private val channelMemberCollection = "members"

    override val channelMemberLocations: Flow<List<ChannelMemberLocation>>
        get() = getChannelMemberLocationsForUser()

    override fun updateUserLocation(location: Position) {
        TODO("Not yet implemented")
    }

    private fun getChannelMemberLocationsForUser(): Flow<List<ChannelMemberLocation>> =
        callbackFlow {
            try {
                val userId = getUserDetails().userId
                val joinedChannels = getJoinedChannels(userId)

                val query =
                    db.collectionGroup(channelMemberCollection).whereIn("channelId", joinedChannels)

                val subscription = query.addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        close(exception)
                        return@addSnapshotListener
                    }

                    val members = querySnapshot?.documents?.mapNotNull { document ->
                        document.toObject<FirebaseChannelMember>()
                    }?.filter { it.id != userId && it.lastLocation != null } ?: emptyList()

                    val channelMembers = members.map {
                        ChannelMemberLocation(
                            it.name,
                            it.channelId,
                            it.timestamp,
                            Position(it.lastLocation!!.latitude, it.lastLocation.longitude)
                        )
                    }

                    trySend(channelMembers).isSuccess
                }

                // Wait for the listener to be removed
                awaitClose { subscription.remove() }
            } catch (e: Exception) {
                trySend(emptyList()).isSuccess
            }
        }

    private suspend fun getJoinedChannels(userId: String): List<String> {
        val userRef = db.collection(userCollection).document(userId)

        val channels = userRef.get().await().get("channels") as? List<*>

        val result = channels?.filterIsInstance<String>()

        return result ?: listOf()
    }

    private fun getUserDetails(): UserDetails {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User is not authenticated")

        return UserDetails(user.uid, user.email.toString(), user.displayName.toString())
    }
}

data class ChannelMemberLocation(
    val name: String, val channelName: String, val timestamp: String, val position: Position
)

