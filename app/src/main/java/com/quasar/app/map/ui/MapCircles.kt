package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.runtime.Composable
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.quasar.app.map.models.Circle
import com.quasar.app.map.styles.MapStyle

@OptIn(MapboxExperimental::class)
@Composable
fun MapCircles(
    circles: List<Circle>, mapStyle: MapStyle, onCircleClicked: (Circle) -> Unit
) {
    MapEffect(circles, mapStyle) {
        Log.d("MapScreen", "Circles list changed")
        it.mapboxMap.getStyle { style ->
            val circleLayers =
                style.styleLayers.filter { layer -> layer.id.startsWith("circle-layer-") }
            val circleSources =
                style.styleSources.filter { layer -> layer.id.startsWith("circle-source-") }

            // TODO - Only draw new and delete old, rather than redrawing every time
            circleLayers.forEach { l -> style.removeStyleLayer(l.id) }
            circleSources.forEach { s -> style.removeStyleSource(s.id) }

            circles.forEach { circle ->
                Log.d("MapScreen", "Adding circle overlay with ID: ${circle.id}")

                val sourceId = "circle-source-${circle.id}"
                val layerId = "circle-layer-${circle.id}"

                val geoJsonCircle = circle.toGeoJsonString()

                style.addSource(geoJsonSource(sourceId) {
                    data(geoJsonCircle)
                })
                style.addLayer(fillLayer(layerId, sourceId) {
                    fillColor(circle.color)
                    fillOpacity(0.4)
                })
            }
        }

    }

    val points = circles.map {
        CircleAnnotationOptions().withPoint(it.center.toPoint()).withCircleRadius(6.0)
            .withCircleColor(it.color).withCircleOpacity(1.0)
            .withDraggable(false) // TODO - Support draggables
    }

    CircleAnnotationGroup(annotations = points, onClick = {
        val circle = circles.first { circle -> circle.center.toPoint() == it.point }
        onCircleClicked(circle)

        true
    })
}