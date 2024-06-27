package com.quasar.app.channels.models

import com.google.firebase.firestore.GeoPoint

data class ChannelMember(
    val id: String = "",
    val channelId: String = "",
    val name: String = "",
    val lastLocation: GeoPoint? = null,
)

data class LastLocation(
    val timestamp: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
