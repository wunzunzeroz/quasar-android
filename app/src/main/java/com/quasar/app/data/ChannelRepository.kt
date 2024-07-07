package com.quasar.app.data

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.quasar.app.domain.models.Channel
import com.quasar.app.domain.models.ChannelMember
import com.quasar.app.domain.models.User
import com.quasar.app.channels.models.CreateChannelInput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface ChannelRepository {
    fun getChannelsForUser(userChannels: Flow<List<String>>): Flow<List<Channel>>
    fun getChannel(channelId: String): Flow<Channel?>
    suspend fun createChannel(input: CreateChannelInput, user: User): String
    suspend fun addMemberToChannel(channelId: String, user: User)
}

class ChannelRepositoryImpl() : ChannelRepository {
    private val db = Firebase.firestore
    private val collectionName = "channels"

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getChannelsForUser(userChannels: Flow<List<String>>): Flow<List<Channel>> {
        return userChannels.flatMapLatest { channelIds ->
            if (channelIds.isEmpty()) {
                flowOf(emptyList())
            } else {
                db.collection(collectionName).whereIn(FieldPath.documentId(), channelIds)
                    .snapshots().mapNotNull { it.toObjects<Channel>() }
            }
        }
    }

    override fun getChannel(channelId: String): Flow<Channel?> {
        return db.collection(collectionName).document(channelId).snapshots()
            .map { it.toObject<Channel>() }
    }

    override suspend fun createChannel(input: CreateChannelInput, user: User): String {
        val channelMember = ChannelMember(userId = user.id, name = user.name)

        val channelId = UUID.randomUUID().toString() // TODO - Create friendly UUIDs

        val channel = Channel(
            channelId, input.name, input.description, members = listOf(channelMember)
        )

        db.collection(collectionName).document(channelId).set(channel).await()

        return channelId
    }

    override suspend fun addMemberToChannel(channelId: String, user: User) {
        val channelMember = ChannelMember(userId = user.id, name = user.name)

//        // TODO - Handle channel doesn't exist
        db.collection(collectionName).document(channelId)
            .update("members", FieldValue.arrayUnion(channelMember)).await()
    }
}
