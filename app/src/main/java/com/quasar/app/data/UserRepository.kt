package com.quasar.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.quasar.app.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await

interface UserRepository {
    fun getUserId(): String

    suspend fun getUser(): User?

    fun getUserChannels(): Flow<List<String>>

    suspend fun addUserToChannel(channelId: String)
}

class UserRepositoryImpl : UserRepository {
    private val db = Firebase.firestore
    private val collectionName = "users"


    override fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid
            ?: throw Exception("Unable to get User ID for current user")
    }

    override suspend fun getUser(): User? {
        return db.collection(collectionName).document(getUserId()).get().await().toObject<User>()
    }

    override fun getUserChannels(): Flow<List<String>> {
        return db.collection(collectionName).document(getUserId()).snapshots()
            .mapNotNull { it.toObject<User>()?.channels ?: emptyList() }
    }

    override suspend fun addUserToChannel(channelId: String) {
        db.collection(collectionName).document(getUserId())
            .update("channels", FieldValue.arrayUnion(channelId)).await()
    }
}