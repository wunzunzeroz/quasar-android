package com.quasar.app.map.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class BottomSheetContentType {
    SelectMapStyle,
    GoToLocation,

    AddWaypoint,
    AddAnnotation,
    AddCircleAnnotation,
    AddPolylineAnnotation,
    AddPolygonAnnotation,

    ViewLocationDetail,
    ViewWaypointDetail,
    ViewPolylineDetail,
}