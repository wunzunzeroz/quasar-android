package com.quasar.app.map.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.WaypointsRepository

class MapViewModel(
    private val waypointsRepository: WaypointsRepository,
    private val circlesRepository: CirclesRepository,
    private val polylinesRepository: PolylinesRepository,
    private val polygonsRepository: PolygonsRepository,
    private val sketchRepository: SketchRepository
) : ViewModel() {
    val test = mutableStateOf("View model is working")
}