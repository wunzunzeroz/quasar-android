package com.quasar.app.map.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.SearchPattern

@OptIn(MapboxExperimental::class)
@Composable
fun MapSearchPatterns(
    searchPatterns: List<SearchPattern>,
    onPatternClicked: (SearchPattern) -> Unit
) {
    val allPoints = searchPatterns.map { it.waypoints.map { pos -> pos.toPoint() } }.flatten()

    val polylineAnnotations = searchPatterns.map {
        PolylineAnnotationOptions().withPoints(it.waypoints.map { pos -> pos.toPoint() })
            .withLineColor(Color.Red.toArgb()).withLineWidth(4.0)
    }
    PolylineAnnotationGroup(polylineAnnotations, onClick = { annotation ->
        val pattern =
            searchPatterns.first { it.waypoints.map { pos -> pos.toPoint() } == annotation.points }

        onPatternClicked(pattern)

        true
    })

    val circleAnnotations = allPoints.map {
        CircleAnnotationOptions().withPoint(it).withCircleColor(Color.Red.toArgb())
            .withCircleRadius(4.0).withCircleOpacity(0.8)
    }
    CircleAnnotationGroup(circleAnnotations, onClick = { annotation ->
        val pattern =
            searchPatterns.first { it.waypoints.contains(Position.fromPoint(annotation.point)) }

        onPatternClicked(pattern)

        true
    })
}