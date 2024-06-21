package com.quasar.app.map.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorLong
import com.mapbox.geojson.Point
import com.quasar.app.map.models.CreateWaypointInput
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.models.WaypointMarkerType
import com.quasar.app.map.utils.Utils
import kotlinx.coroutines.launch

@Composable
fun ViewWaypointDetailSheet(
    waypoint: Waypoint,
    onUpdateWaypoint: (Waypoint) -> Unit,
    onDeleteWaypoint: (Waypoint) -> Unit,
    modifier: Modifier = Modifier
) {
    var latitude by remember { mutableDoubleStateOf(waypoint.position.latLngDecimal.latitude) }
    var longitude by remember { mutableDoubleStateOf(waypoint.position.latLngDecimal.longitude) }
    var name by remember { mutableStateOf(waypoint.name) }
    var code by remember { mutableStateOf(waypoint.code) }
    var markerType by remember { mutableStateOf(waypoint.markerType) }
    var color by remember { mutableStateOf(Color.Magenta) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(waypoint.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            WaypointIconDropdown(onValueChange = { markerType = it })
            ColorSelectDropdown(initialValue = color, onValueChange = {})
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Shortcode") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            TextField(
                value = Utils.RoundNumberToDp(latitude, 6).toString(),
                onValueChange = { latitude = it.toDouble() },
                label = { Text("Latitude") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1.0f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = Utils.RoundNumberToDp(longitude, 6).toString(),
                onValueChange = { longitude = it.toDouble() },
                label = { Text("Longitude") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1.0f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                val newWaypoint = waypoint
                    .withPosition(Position(latitude, longitude))
                    .withName(name)
                    .withCode(code)
                    .withMarkerType(markerType)
                    .withColor(color)

                Log.d("AddWaypointSheet", "Creating waypoint '$name' at $latitude, $longitude")

                onUpdateWaypoint(newWaypoint)
            }, modifier = Modifier) {
                Text("Update Waypoint")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(onClick = {
                onDeleteWaypoint(waypoint)
            }) {
                Text("Delete Waypoint")
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun ViewWaypointDetailSheetPreview() {
    ViewWaypointDetailSheet(
        waypoint = Waypoint(
            0,
            Position(-36.0, 174.0),
            "Rangitoto",
            "RANGI",
            WaypointMarkerType.Castle,
            Utils.convertColorToHexString(Color.Magenta)
        ),
        onUpdateWaypoint = {},
        onDeleteWaypoint = {}
    )
}