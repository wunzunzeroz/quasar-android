package com.quasar.app.domain.models

/**
 * The member details for a user that has joined a channel
 *
 * Found in Firestore channel.members array
 */
data class ChannelMember(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
)
