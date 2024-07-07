package com.quasar.app.domain.models

import com.google.firebase.firestore.DocumentId

data class Channel(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val members: List<ChannelMember> = listOf()
)
