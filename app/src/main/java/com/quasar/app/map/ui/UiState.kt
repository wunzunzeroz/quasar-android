package com.quasar.app.map.ui

import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Sketch
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.styles.MapStyle

data class UiState(
    val bottomSheetVisible: Boolean = false,
    val bottomSheetType: BottomSheetContentType = BottomSheetContentType.SelectMapStyle,

    val mapStyle: MapStyle = MapStyle.Outdoors,

    val waypoints: List<Waypoint> = listOf(),
    val circles: List<Circle> = listOf(),
    val polylines: List<Polyline> = listOf(),
    val polygons: List<Polygon> = listOf(),
    val sketches: List<Sketch> = listOf()
)
