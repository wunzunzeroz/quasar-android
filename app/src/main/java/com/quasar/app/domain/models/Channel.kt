package com.quasar.app.domain.models

import com.quasar.app.channels.models.FirebaseChannelMember

data class Channel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val memberCount: Int = 0,
    val members: List<FirebaseChannelMember> = listOf()
)
