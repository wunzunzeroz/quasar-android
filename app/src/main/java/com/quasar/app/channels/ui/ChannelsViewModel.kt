package com.quasar.app.channels.ui

import androidx.lifecycle.ViewModel
import com.quasar.app.channels.data.ChannelRepository
import com.quasar.app.channels.models.Channel
import com.quasar.app.channels.models.CreateChannelInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ChannelsViewModel(private val channelRepository: ChannelRepository) : ViewModel() {
    val uiState = MutableStateFlow(UiState())

    val channels = channelRepository.channels
    val members = channelRepository.lastLocations

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
        return channelRepository.createChannel(channel)
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
        channelRepository.joinChannel(channelId)
    }

    suspend fun getChannel(channelId: String): Channel? {
        return channelRepository.getChannel(channelId)
    }
}