package com.quasar.app.domain.services

import com.quasar.app.channels.models.CreateChannelInput
import com.quasar.app.data.ChannelRepository
import com.quasar.app.data.UserRepository
import com.quasar.app.domain.models.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelService {
    fun getChannels(): Flow<List<Channel>>
    fun getChannel(channelId: String): Flow<Channel?>
    suspend fun createChannel(input: CreateChannelInput): String
    suspend fun joinChannel(channelId: String)
    suspend fun leaveChannel(channelId: String)
    suspend fun updateChannel()
}

class ChannelServiceImpl(
    private val channelRepository: ChannelRepository,
    private val userRepository: UserRepository
) : ChannelService {
    override fun getChannels(): Flow<List<Channel>> {
        val userChannels = userRepository.getUserChannels()
        return channelRepository.getChannelsForUser(userChannels)
    }

    override fun getChannel(channelId: String): Flow<Channel?> {
        return channelRepository.getChannel(channelId)
    }

    override suspend fun createChannel(input: CreateChannelInput): String {
        val user = userRepository.getUser()
        val channelId = channelRepository.createChannel(input, user)
        userRepository.addUserToChannel(channelId)

        return channelId
    }

    override suspend fun joinChannel(channelId: String) {
        val user = userRepository.getUser()
        channelRepository.addMemberToChannel(channelId, user)
        userRepository.addUserToChannel(channelId)
    }

    override suspend fun leaveChannel(channelId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateChannel() {
        TODO("Not yet implemented")
    }

}