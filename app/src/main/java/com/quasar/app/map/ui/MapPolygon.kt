package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolygon(points: List<Point>, color: Color = Color.Magenta) {
    Log.d("MapScreen", "Point count: ${points.count()}")

    val polylinePoints = points.map {
        CircleAnnotationOptions().withPoint(it).withCircleRadius(4.0)
            .withCircleColor(color.toArgb()).withCircleOpacity(0.7)
    }
    PolygonAnnotation(
        points = listOf(points), fillColorInt = color.toArgb(), fillOpacity = 0.5
    )
    CircleAnnotationGroup(annotations = polylinePoints)
}