package com.quasar.app.channels.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quasar.app.channels.models.Channel
import com.quasar.app.channels.models.CreateChannelInput
import com.quasar.app.channels.models.UserDetails
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


interface ChannelRepository {
    val channels: Flow<List<Channel>>
    suspend fun createChannel(channel: CreateChannelInput): String
    suspend fun joinChannel(channelId: String)
}

class ChannelRepositoryImpl() : ChannelRepository {
    private val channelCollection = "channels"
    private val userCollection = "users"

    private val db = Firebase.firestore

    override val channels: Flow<List<Channel>>
        get() = getUserChannels()

    private fun getUserChannels(): Flow<List<Channel>> = callbackFlow {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        val query = db.collection(channelCollection).whereArrayContains("members", userId)

        val subscription = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val channels = querySnapshot?.documents?.mapNotNull { document ->
                document.toObject(Channel::class.java)
            } ?: emptyList()

            trySend(channels)
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun createChannel(channel: CreateChannelInput): String {
        val userDetails = getUserDetails()
        addUserIfNotExists()

        val channelRef = db.collection(channelCollection).add(channel).await()

        // Add user ID to channel's 'members' array
        db.collection(channelCollection).document(channelRef.id)
            .update("members", FieldValue.arrayUnion(userDetails.userId)).await()

        // Add channel ID to user object's 'channels' array
        db.collection(userCollection).document(userDetails.userId)
            .update("channels", FieldValue.arrayUnion(channelRef.id)).await()

        return channelRef.id
    }

    override suspend fun joinChannel(channelId: String) {
        val userDetails = getUserDetails()

        // TODO - Handle channel doesn't exist
        val channelRef = db.collection(channelCollection).document(channelId)

        // Add user Id to channel's 'members' array
        channelRef.update("members", FieldValue.arrayUnion(userDetails.userId)).await()
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
