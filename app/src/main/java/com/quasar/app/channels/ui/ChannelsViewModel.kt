package com.quasar.app.channels.ui

import androidx.lifecycle.ViewModel
import com.quasar.app.data.ChannelRepository
import com.quasar.app.domain.models.Channel
import com.quasar.app.channels.models.CreateChannelInput
import com.quasar.app.domain.services.ChannelService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ChannelsViewModel(private val channelService: ChannelService) : ViewModel() {
    val uiState = MutableStateFlow(UiState())

    fun getChannels(): Flow<List<Channel>> {
        return channelService.getChannels()
    }

    fun showCreateChannelSheet() {
        uiState.update { current ->
            current.copy(
                bottomSheetVisible = true,
                bottomSheetType = BottomSheetContentType.CreateChannel
            )
        }
    }

    fun hideBottomSheet() {
        uiState.update { current ->
            current.copy(bottomSheetVisible = false)
        }
    }

    suspend fun createChannel(channel: CreateChannelInput): String {
        return channelService.createChannel(channel)
    }

    fun showJoinChannelSheet() {
        uiState.update { current ->
            current.copy(
                bottomSheetVisible = true,
                bottomSheetType = BottomSheetContentType.JoinChannel
            )
        }
    }

    suspend fun joinChannel(channelId: String) {
        channelService.joinChannel(channelId)
    }

    fun getChannel(channelId: String): Flow<Channel?> {
        return channelService.getChannel(channelId)
    }
}