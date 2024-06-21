package com.quasar.app.map.ui

import androidx.compose.runtime.Composable
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.quasar.app.map.models.Polygon

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolygons(
    polygons: List<Polygon>, onLineClicked: (Polygon) -> Unit
) {
    val allPoints = mutableListOf<Point>()
    polygons.forEach { allPoints.addAll(it.points()) }

    val pts = polygons.map {
        it.points().map { pt ->
            CircleAnnotationOptions().withPoint(pt).withCircleRadius(4.0).withCircleColor(it.color)
                .withCircleOpacity(0.7)
        }
    }.flatten()

    val polys = polygons.map {
        PolygonAnnotationOptions().withPoints(listOf(it.points())).withFillColor(it.color)
            .withFillOpacity(0.5)
    }

    PolygonAnnotationGroup(annotations = polys, onClick = {
        val poly = polygons.first { poly -> poly.points() == it.points.first() }
        onLineClicked(poly)

        true
    })

    CircleAnnotationGroup(annotations = pts, onClick = {
        val poly = polygons.first { poly -> poly.points().contains(it.point) }
        onLineClicked(poly)

        true
    })
}