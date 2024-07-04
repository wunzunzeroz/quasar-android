package com.quasar.app.channels.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.quasar.app.channels.models.Channel
import com.quasar.app.channels.models.FirebaseChannelMember
import com.quasar.app.channels.models.CreateChannelInput
import com.quasar.app.channels.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.util.UUID


interface ChannelRepository {
    val channels: Flow<List<Channel>>

    suspend fun createChannel(channelInput: CreateChannelInput): String
    suspend fun joinChannel(channelId: String)
    fun getChannel(channelId: String): Flow<Channel?>
}

class ChannelRepositoryImpl() : ChannelRepository {
    private val channelCollection = "channels"
    private val userCollection = "users"

    private val db = Firebase.firestore

    override val channels: Flow<List<Channel>>
        get() = getChannels(getUserDetails().userId)

    private fun getJoinedChannels(userId: String): Flow<List<String>> =
        db.collection("users").document(userId).snapshots()
            .mapNotNull { it.toObject<User>()?.channels ?: emptyList() }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getChannels(userId: String): Flow<List<Channel>> =
        getJoinedChannels(userId).flatMapLatest { joinedChannels ->
            db.collection("channels").whereIn(FieldPath.documentId(), joinedChannels).snapshots()
                .mapNotNull { it.toObjects<Channel>() }
        }

    override fun getChannel(channelId: String): Flow<Channel?> {
        return db.collection("channels").document(channelId).snapshots()
            .map { it.toObject<Channel>() }
    }

    override suspend fun createChannel(channelInput: CreateChannelInput): String {
        val userDetails = getUserDetails()
        addUserIfNotExists()

        val firebaseChannelMember = FirebaseChannelMember(userDetails.userId, userDetails.name)

        val channelId = UUID.randomUUID().toString()
        val channel =
            Channel(channelId, channelInput.name, channelInput.description, memberCount = 1)
        db.collection(channelCollection).document(channelId).set(channel).await()

        // Add user ID to channel's 'members' array
        db.collection(channelCollection).document(channelId).collection("members")
            .document(userDetails.userId).set(firebaseChannelMember).await()

        // Add channel ID to user object's 'channels' array
        db.collection(userCollection).document(userDetails.userId)
            .update("channels", FieldValue.arrayUnion(channelId)).await()

        return channelId
    }

    override suspend fun joinChannel(channelId: String) {
        val userDetails = getUserDetails()
        val firebaseChannelMember = FirebaseChannelMember(userDetails.userId, userDetails.name)

        // TODO - Handle channel doesn't exist
        val channelRef = db.collection(channelCollection).document(channelId)

        channelRef.collection("members").document(userDetails.userId).set(firebaseChannelMember)
            .await()
        channelRef.update("memberCount", FieldValue.increment(1)).await()
    }

    private suspend fun addUserIfNotExists() {
        val userDetails = getUserDetails()
        val userDocRef = db.collection(userCollection).document(userDetails.userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            if (!snapshot.exists()) {
                transaction.set(userDocRef, userDetails)
            }
        }.await()
    }

    private fun getUserDetails(): UserDetails {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User is not authenticated")

        return UserDetails(user.uid, user.email.toString(), user.displayName.toString())
    }
}

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val channels: List<String> = emptyList()
)
