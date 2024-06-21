package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolyline(polyline: List<Point>, color: Color = Color.Magenta) {
    Log.d("MapScreen", "Point count: ${polyline.count()}")

    val polylinePoints = polyline.map {
        CircleAnnotationOptions().withPoint(it).withCircleRadius(4.0)
            .withCircleColor(color.toArgb()).withCircleOpacity(0.7)
    }
    PolylineAnnotation(
        polyline, lineWidth = 3.0, lineColorInt = color.toArgb(), lineOpacity = 0.5
    )
    CircleAnnotationGroup(annotations = polylinePoints)
}