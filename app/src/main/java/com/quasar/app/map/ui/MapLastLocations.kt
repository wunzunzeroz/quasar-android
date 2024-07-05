package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.ViewAnnotationOptions
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.quasar.app.map.data.UserLocation
import com.quasar.app.map.models.WaypointMarkerType

@OptIn(MapboxExperimental::class)
@Composable
fun MapLastLocations(lastLocations: List<UserLocation>) {
    Log.d("MapLastLocations", "Adding ${lastLocations.count()} user locations to map:")
    lastLocations.forEach {
        Log.d(
            "MapLastLocations",
            "\tUser ID: ${it.userId}, Location (lat/lng): ${it.position.latitude}/${it.position.longitude}"
        )
    }
    val waypointAnnotations = lastLocations.map {
        PointAnnotationOptions().withPoint(
            Point.fromLngLat(
                it.position.longitude,
                it.position.latitude
            )
        )
            .withIconImage(getMarkerBitmap(LocalContext.current, WaypointMarkerType.Person))
    }

    val circleAnnotations = lastLocations.map {
        CircleAnnotationOptions().withPoint(
            Point.fromLngLat(
                it.position.longitude,
                it.position.latitude
            )
        )
            .withCircleColor(Color.Magenta.toArgb()).withCircleRadius(10.0).withCircleOpacity(0.7)
    }

    CircleAnnotationGroup(annotations = circleAnnotations, onClick = {
        val cml = lastLocations.first { ll ->
            Point.fromLngLat(
                ll.position.longitude,
                ll.position.latitude
            ) == it.point
        }

        Log.d("MapScreen", "Tapped ${cml.userId}")

        true
    })

    PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
        true
    })

    lastLocations.forEach { loc ->
        ViewAnnotation(options = viewAnnotationOptions {
            geometry(Point.fromLngLat(loc.position.longitude, loc.position.latitude))
            annotationAnchor { anchor(ViewAnnotationAnchor.TOP) }
        }
        ) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                Text(loc.userName, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}