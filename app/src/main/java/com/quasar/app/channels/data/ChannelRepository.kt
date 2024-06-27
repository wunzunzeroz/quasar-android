package com.quasar.app.channels.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.quasar.app.channels.models.Channel
import com.quasar.app.channels.models.FirebaseChannelMember
import com.quasar.app.channels.models.CreateChannelInput
import com.quasar.app.channels.models.UserDetails
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID


interface ChannelRepository {
    val channels: Flow<List<Channel>>

    suspend fun createChannel(channelInput: CreateChannelInput): String
    suspend fun joinChannel(channelId: String)
    suspend fun getChannel(channelId: String): Channel?
}

class ChannelRepositoryImpl() : ChannelRepository {
    private val channelCollection = "channels"
    private val userCollection = "users"

    private val db = Firebase.firestore

    override val channels: Flow<List<Channel>>
        get() = getUserChannels()

    override suspend fun getChannel(channelId: String): Channel? {
        val channelRef = db.collection(channelCollection).document(channelId).get().await()
        val membersRef =
            db.collection(channelCollection).document(channelId).collection("members").get().await()

        val channel = channelRef.toObject<Channel>()
        val members = membersRef.toObjects<FirebaseChannelMember>()

        if (channel == null) {
            return null
        }

        val result =
            Channel(channel.id, channel.name, channel.description, channel.memberCount, members)

        return result
    }

    private fun getUserChannels(): Flow<List<Channel>> = callbackFlow {
        try {
            addUserIfNotExists()
            val userId = getUserDetails().userId

            // Get the user's document from the "users" collection
            val userDocRef = db.collection("users").document(userId)

            val subscription = userDocRef.addSnapshotListener { userSnapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val channelIds = userSnapshot?.get("channels") as? List<String> ?: emptyList()

                if (channelIds.isEmpty()) {
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }

                // Get all channel documents that match the channel IDs
                db.collection("channels").whereIn(FieldPath.documentId(), channelIds).get()
                    .addOnSuccessListener { querySnapshot ->
                        val channels = querySnapshot?.toObjects(Channel::class.java) ?: emptyList()
                        trySend(channels).isSuccess
                    }.addOnFailureListener { e ->
                        close(e)
                    }
            }

            awaitClose { subscription.remove() }
        } catch (e: Exception) {
            // Log the error for debugging purposes
            println("Error fetching user channels: ${e.message}")
            trySend(emptyList()).isSuccess
            close(e)
        }
    }

    private fun getUserChannels2(): Flow<List<Channel>> = callbackFlow {
        addUserIfNotExists()
        val userId = getUserDetails().userId

        // Get the user's document from the "users" collection
        val userDocRef = db.collection("users").document(userId)

        val subscription = userDocRef.addSnapshotListener { userSnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }


            val channelIds = userSnapshot?.get("channels") as? List<*> ?: emptyList<String>()

            if (channelIds.isEmpty()) {
                trySend(emptyList())
                return@addSnapshotListener
            }

            // Get all channel documents that match the channel IDs
            db.collection("channels").whereIn(FieldPath.documentId(), channelIds).get()
                .addOnSuccessListener { querySnapshot ->
                    val channels = querySnapshot?.toObjects(Channel::class.java) ?: emptyList()
                    trySend(channels)
                }.addOnFailureListener { e ->
                    close(e)
                }
        }

        awaitClose { subscription.remove() }
    }

    private suspend fun getJoinedChannels(userId: String): List<String> {
        val userRef = db.collection(userCollection).document(userId)

        val channels = userRef.get().await().get("channels") as? List<*>

        val result = channels?.filterIsInstance<String>()

        return result ?: listOf()
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
