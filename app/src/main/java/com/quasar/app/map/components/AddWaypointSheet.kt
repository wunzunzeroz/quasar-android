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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorLong
import com.mapbox.geojson.Point
import com.quasar.app.R
import com.quasar.app.map.models.CreateWaypointInput
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.WaypointMarkerType
import com.quasar.app.map.ui.getDrawableForWaypointMarker
import com.quasar.app.map.utils.Utils

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Create Waypoint", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        Row {

            WaypointIconDropdown(onValueChange = { markerType = it })
//            Spacer(modifier = Modifier.width(8.dp))
//
            // TODO - Handle color selection
//            ColorSelectDropdown(onValueChange = { markerColor = it })
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


@Composable
fun WaypointIconDropdown(onValueChange: (WaypointMarkerType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(WaypointMarkerType.Flag) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { expanded = true },
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Marker Type:")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(painterResource(getDrawableForWaypointMarker(selectedType)), "")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(300.dp)
        ) {
            WaypointMarkerType.entries.forEach { type ->
                DropdownMenuItem(onClick = {
                    onValueChange(type)
                    selectedType = type
                    expanded = false
                }, text = {
                    Row {
                        Icon(
                            painterResource(getDrawableForWaypointMarker(type)),
                            "Marker Type"
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = type.name)
                    }
                })
            }
        }
    }
}

@Composable
fun ColorSelectDropdown(onValueChange: (Color) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(Pair("Magenta", Color.Magenta)) }


    val colors = listOf(
        Pair("Blue", Color.Blue),
        Pair("Green", Color.Green),
        Pair("Magenta", Color.Magenta),
        Pair("Yellow", Color.Yellow),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { expanded = true },
        ) {
            Text("Marker Color: ${selected.first}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(300.dp)
        ) {
            colors.forEach { color ->
                DropdownMenuItem(onClick = {
                    onValueChange(color.second)
                    selected = color
                    expanded = false
                }, text = { Text(color.first) })
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddWaypointSheetPreview() {
    AddWaypointSheet(location = Point.fromLngLat(69.0, 42.9), onCreateWaypoint = {})
}