package com.quasar.app.channels.data

import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.quasar.app.channels.models.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await


interface ChannelRepository {
    val channels: Flow<List<Channel>>
    suspend fun createChannel(channel: Channel): String
}

class ChannelRepositoryImpl() : ChannelRepository {
    private val collectionName = "channels"
    private val db = Firebase.firestore

    override val channels: Flow<List<Channel>>
        get() = db.collection(collectionName).dataObjects()

    override suspend fun createChannel(channel: Channel): String {
        val documentReference = db.collection(collectionName)
            .add(channel)
            .await()

        return documentReference.id
    }
}