package com.quasar.app.map.ui

import androidx.compose.runtime.Composable
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.quasar.app.map.models.Polyline

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolylines(polylines: List<Polyline>, onLineClicked: (Polyline) -> Unit) {
    val lines = polylines.map {
        PolylineAnnotationOptions().withPoints(it.points()).withLineColor(it.color)
            .withLineWidth(3.0)
    }

    val pts = polylines.map {
        it.points().map { pt ->
            CircleAnnotationOptions().withPoint(pt).withCircleRadius(6.0)
                .withCircleColor(it.color).withDraggable(false) // TODO - Support draggables
        }
    }.flatten()

    PolylineAnnotationGroup(annotations = lines, onClick = {
        val line = polylines.first { line -> line.points() == it.points }
        onLineClicked(line)

        true
    })
    CircleAnnotationGroup(annotations = pts, onClick = {
        val line = polylines.first { line -> line.points().contains(it.point) }
        onLineClicked(line)

        true
    })
}