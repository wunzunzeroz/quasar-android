package com.quasar.app.channels.models

data class ChannelMember(
    val id: String = "",
    val name: String = "",
    val lastLocation: LastLocation? = null,
)

data class LastLocation(
    val timestamp: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
