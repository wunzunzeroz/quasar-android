package com.quasar.app.map.components

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import com.quasar.app.R
import com.quasar.app.map.models.CreateWaypointInput
import com.quasar.app.map.models.Position
import com.quasar.app.map.styles.MapStyle
import com.quasar.app.map.ui.CoordinateType
import com.quasar.app.map.utils.Utils

@Composable
fun LocationDetailSheet(
    userLocation: Point,
    location: Point,
    onCreateWaypoint: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    var coordinateType by remember { mutableStateOf(CoordinateType.LatLngDec) } // TODO - Persist as user pref?
    val position = Position.fromPoint(location)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Tapped Location", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(4.dp))
        CoordinateTypeSelector(value = coordinateType, onSelect = { coordinateType = it })
        Spacer(modifier = Modifier.height(16.dp))

        when (coordinateType) {
            CoordinateType.LatLngDec -> LatLngDecimalLocation(position = position)
            CoordinateType.LatLngDms -> LatLngDmsLocation(position = position)
            CoordinateType.GridRef -> GridRefLocation(position = position)
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    String.format("%.1f km", calculateDistance(userLocation, location)),
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Distance", style = MaterialTheme.typography.labelLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    String.format("%.0f °T", calculateBearing(userLocation, location)),
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Bearing", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onCreateWaypoint()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Waypoint")
        }
        OutlinedButton(
            onClick = { shareLocation(ctx, location) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share Location")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun calculateDistance(point1: Point, point2: Point): Double {
    return TurfMeasurement.distance(point1, point2, TurfConstants.UNIT_KILOMETERS)
}

fun calculateBearing(point1: Point, point2: Point): Double {
    val result = TurfMeasurement.bearing(point1, point2)

    return if (result < 0) 360 + result else result
}

private fun shareLocation(context: Context, location: Point) {
    val locationString = "${location.latitude()}, ${location.longitude()}"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Location Details")
        putExtra(Intent.EXTRA_TEXT, locationString)
    }

    context.startActivity(Intent.createChooser(intent, "Share Location"))

}

@Composable
fun LatLngDecimalLocation(position: Position) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                String.format("%.6f", position.latLngDecimal.latitude),
                style = MaterialTheme.typography.titleLarge
            )
            Text("Latitude", style = MaterialTheme.typography.labelLarge)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                String.format("%.6f", position.latLngDecimal.longitude),
                style = MaterialTheme.typography.titleLarge
            )
            Text("Longitude", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun LatLngDmsLocation(position: Position) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Text(
                    "${position.latLngDegreesMinutes.latitude.degrees}°",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${position.latLngDegreesMinutes.latitude.minutes}'",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${position.latLngDegreesMinutes.latitude.cardinalDirection}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text("Latitude", style = MaterialTheme.typography.labelLarge)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Text(
                    "${position.latLngDegreesMinutes.longitude.degrees}°",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${position.latLngDegreesMinutes.longitude.minutes}'",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${position.latLngDegreesMinutes.longitude.cardinalDirection}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text("Longitude", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun GridRefLocation(position: Position) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "${position.gridReference.eastings} E",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    "${position.gridReference.northings} N",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text("Grid Reference (NZTM)", style = MaterialTheme.typography.labelLarge)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun LocationDetailSheetPreview() {
    LocationDetailSheet(
        userLocation = Point.fromLngLat(68.987654, 44.987654),
        location = Point.fromLngLat(174.8480085, -36.832527),
        onCreateWaypoint = {}
    )
}