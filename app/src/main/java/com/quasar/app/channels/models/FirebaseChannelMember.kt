package com.quasar.app.channels.models

import com.google.firebase.firestore.GeoPoint

data class FirebaseChannelMember(
    val id: String = "",
    val channelId: String = "",
    val name: String = "",
    val timestamp: String = "",
    val lastLocation: GeoPoint? = null,
)
