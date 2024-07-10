package com.quasar.app.channels.ui

data class UiState(
    val bottomSheetVisible: Boolean = false,
    val bottomSheetType: BottomSheetContentType = BottomSheetContentType.CreateChannel,
)
