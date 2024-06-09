package com.quasar.app.map.components

import android.util.Log
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
import com.quasar.app.map.models.WaypointMarkerType
import kotlinx.coroutines.launch

@Composable
fun AddWaypointSheet(
    location: Point, onCreateWaypoint: (CreateWaypointInput) -> Unit, modifier: Modifier = Modifier
) {
    var latitude by remember { mutableDoubleStateOf(location.latitude()) }
    var longitude by remember { mutableDoubleStateOf(location.longitude()) }
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var markerType by remember { mutableStateOf(WaypointMarkerType.Marker) }
    var markerColor by remember { mutableStateOf(Color.Magenta) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Create Waypoint", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(32.dp))


        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Shortcode") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            TextField(
                value = latitude.toString(),
                onValueChange = { latitude = it.toDouble() },
                label = { Text("Latitude") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1.0f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = longitude.toString(),
                onValueChange = { longitude = it.toDouble() },
                label = { Text("Longitude") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1.0f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))


        Button(onClick = {
            val input = CreateWaypointInput(
                Position(latitude, longitude),
                name,
                code,
                markerType,
                markerColor.toArgb().toColorLong()
            )

            Log.d("AddWaypointSheet", "Creating waypoint '$name' at $latitude, $longitude")

            onCreateWaypoint(input)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Waypoint")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AddWaypointSheetPreview() {
    AddWaypointSheet(location = Point.fromLngLat(69.0, 42.9), onCreateWaypoint = {})
}