package com.quasar.app.domain.models

import com.google.firebase.firestore.GeoPoint

data class UserLocation(
    val userId: String = "",
    val userName: String = "",
    val channelIds: List<String> = listOf(),
    val timestamp: String = "",
    val position: GeoPoint = GeoPoint(0.0, 0.0),
)