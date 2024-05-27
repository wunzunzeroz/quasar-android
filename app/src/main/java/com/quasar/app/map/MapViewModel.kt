package com.quasar.app.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    val test = mutableStateOf("View model is working")
}