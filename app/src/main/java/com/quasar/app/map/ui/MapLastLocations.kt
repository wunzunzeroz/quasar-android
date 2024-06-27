package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.quasar.app.map.data.ChannelMemberLocation
import com.quasar.app.map.models.WaypointMarkerType

@OptIn(MapboxExperimental::class)
@Composable
fun MapLastLocations(lastLocations: List<ChannelMemberLocation>) {
    val waypointAnnotations = lastLocations.map {
        PointAnnotationOptions().withPoint(it.position.toPoint())
            .withIconImage(getMarkerBitmap(LocalContext.current, WaypointMarkerType.Person))
    }

    val circleAnnotations = lastLocations.map {
        CircleAnnotationOptions().withPoint(it.position.toPoint())
            .withCircleColor(Color.Magenta.toArgb()).withCircleRadius(10.0).withCircleOpacity(0.7)
    }

    CircleAnnotationGroup(annotations = circleAnnotations, onClick = {
        val cml = lastLocations.first { ll -> ll.position.toPoint() == it.point }

        Log.d("MapScreen", "Tapped ${cml.name}")

        true
    })

    PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
        true
    })
}