package com.quasar.app.channels.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quasar.app.channels.data.ChannelRepository
import com.quasar.app.channels.models.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChannelsViewModel(private val channelRepository: ChannelRepository) : ViewModel() {
    val uiState = MutableStateFlow(UiState())

    val chnls = channelRepository.channels

    val channels: StateFlow<List<Channel>> =
        channelRepository.channels.map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

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

    suspend fun createChannel(channel: Channel): String {
        return channelRepository.createChannel(channel)
    }
}