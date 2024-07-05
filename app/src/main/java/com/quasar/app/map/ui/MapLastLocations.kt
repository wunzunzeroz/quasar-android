package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.quasar.app.map.data.UserLocation
import com.quasar.app.map.models.WaypointMarkerType

@OptIn(MapboxExperimental::class)
@Composable
fun MapLastLocations(lastLocations: List<UserLocation>) {
    val waypointAnnotations = lastLocations.map {
        PointAnnotationOptions().withPoint(Point.fromLngLat(it.position.longitude, it.position.latitude))
            .withIconImage(getMarkerBitmap(LocalContext.current, WaypointMarkerType.Person))
    }

    val circleAnnotations = lastLocations.map {
        CircleAnnotationOptions().withPoint(Point.fromLngLat(it.position.longitude, it.position.latitude))
            .withCircleColor(Color.Magenta.toArgb()).withCircleRadius(10.0).withCircleOpacity(0.7)
    }

    CircleAnnotationGroup(annotations = circleAnnotations, onClick = {
        val cml = lastLocations.first { ll -> Point.fromLngLat(ll.position.longitude, ll.position.latitude) == it.point }

        Log.d("MapScreen", "Tapped ${cml.userId}")

        true
    })

    PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
        true
    })
}