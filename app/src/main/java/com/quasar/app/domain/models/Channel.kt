package com.quasar.app.domain.models

data class Channel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val members: List<ChannelMember> = listOf()
)
