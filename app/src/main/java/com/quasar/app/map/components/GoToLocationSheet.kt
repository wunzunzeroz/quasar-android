package com.quasar.app.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.quasar.app.map.models.CardinalDirection
import com.quasar.app.map.models.GridReference
import com.quasar.app.map.models.Position
import com.quasar.app.map.ui.CoordinateType
import com.quasar.app.map.ui.DistanceUnit

@Composable
fun GoToLocationSheet(onSubmit: (Position) -> Unit, modifier: Modifier = Modifier) {
    var coordinateType by remember { mutableStateOf(CoordinateType.LatLngDec) }
    var position by remember { mutableStateOf<Position?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Go to Location", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(4.dp))

        CoordinateTypeSelector(value = coordinateType, onSelect = { coordinateType = it })

        Spacer(modifier = Modifier.height(16.dp))

        PositionInput(type = coordinateType, onSubmit = { position = it })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            position?.let {
                onSubmit(it)
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Go to location")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PositionInput(
    type: CoordinateType,
    onSubmit: (Position) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Position? = null,
) {
    Column {
        when (type) {
            CoordinateType.LatLngDec -> LatLngDecInput(onSubmit, initialValue)
            CoordinateType.LatLngDms -> LatLngDdmInput(onSubmit, initialValue)
            CoordinateType.GridRef -> GridRefInput(onSubmit, initialValue)
        }
    }
}

@Composable
fun LatLngDecInput(onSubmit: (Position) -> Unit, initialValue: Position?) {
    var latitude by remember { mutableStateOf(initialValue?.latLngDecimal?.latitude?.toString() ?: "") }
    var longitude by remember { mutableStateOf(initialValue?.latLngDecimal?.longitude?.toString() ?: "") }

    fun checkSubmit() {
        if (latitude.toDoubleOrNull() != null && longitude.toDoubleOrNull() != null) {
            val pos = Position(latitude.toDouble(), longitude.toDouble())

            onSubmit(pos)
        }
    }

    Row {
        TextField(
            value = latitude,
            onValueChange = {
                latitude = it
                checkSubmit()
            },
            label = { Text("Latitude") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            modifier = Modifier.weight(1.0f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = longitude,
            onValueChange = {
                longitude = it
                checkSubmit()
            },
            label = { Text("Longitude") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            modifier = Modifier.weight(1.0f)
        )
    }
}

@Composable
fun LatLngDdmInput(onSubmit: (Position) -> Unit, initialValue: Position?) {
    var latDeg by remember { mutableStateOf(initialValue?.latLngDegreesMinutes?.latitude?.degrees?.toString()
        ?: "") }
    var latMin by remember { mutableStateOf(initialValue?.latLngDegreesMinutes?.latitude?.minutes?.toString() ?: "") }
    var latDir by remember { mutableStateOf(CardinalDirection.S) }

    var lngDeg by remember { mutableStateOf(initialValue?.latLngDegreesMinutes?.longitude?.degrees?.toString() ?: "") }
    var lngMin by remember { mutableStateOf(initialValue?.latLngDegreesMinutes?.longitude?.minutes?.toString() ?: "") }
    var lngDir by remember { mutableStateOf(CardinalDirection.E) }


    fun checkSubmit() {
        val latD = latDeg.toIntOrNull()
        val latM = latMin.toDoubleOrNull()

        val lngD = lngDeg.toIntOrNull()
        val lngM = lngMin.toDoubleOrNull()

        if (latD != null && latM != null && lngD != null && lngM != null) {
            val pos = Position.fromDegreesMinutes(latD, latM, latDir, lngD, lngM, lngDir);

            onSubmit(pos)
        }
    }

    Column {
        Row {
            TextField(
                value = latDeg,
                onValueChange = {
                    latDeg = it
                    checkSubmit()
                },
                label = { Text("Lat Degrees") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = latMin,
                onValueChange = {
                    latMin = it
                    checkSubmit()
                },
                label = { Text("Lat Minutes") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            DirectionDropdown(value = latDir, onSelect = { latDir = it }, modifier = Modifier.weight(0.5f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            TextField(
                value = lngDeg,
                onValueChange = {
                    lngDeg = it
                    checkSubmit()
                },
                label = { Text("Lng Degrees") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = lngMin,
                onValueChange = {
                    lngMin = it
                    checkSubmit()
                },
                label = { Text("Lng Minutes") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            DirectionDropdown(value = lngDir, onSelect = { lngDir = it }, modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun DirectionDropdown(value: CardinalDirection, onSelect: (CardinalDirection) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    TextField(
        value = value.name,
        readOnly = true,  // Make the text field read-only
        onValueChange = {},
        label = { Text("Dir") },
        trailingIcon = {
            Icon(imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                Modifier.clickable { expanded = true })
        },
        modifier = modifier)
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        CardinalDirection.entries.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSelect(item)
                },
                text = {
                    Text(item.name)
                })
        }
    }
}

@Composable
fun GridRefInput(onSubmit: (Position) -> Unit, initialValue: Position?) {
    var eastings by remember { mutableStateOf(initialValue?.gridReference?.eastings.toString()) }
    var northings by remember { mutableStateOf(initialValue?.gridReference?.northings.toString()) }

    fun checkSubmit() {
        if (eastings.toIntOrNull() != null && northings.toIntOrNull() != null) {
            val gr = GridReference(eastings.toInt(), northings.toInt())
            val position = Position.fromGridRef(gr)
            onSubmit(position)
        }
    }

    Row {
        TextField(value = eastings.toString(), onValueChange = {
            eastings = it
            checkSubmit()
        }, label = { Text("Eastings") }, keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
        ), modifier = Modifier.weight(1.0f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(value = northings.toString(), onValueChange = {
            northings = it
            checkSubmit()
        }, label = { Text("Northings") }, keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ), modifier = Modifier.weight(1.0f)
        )
    }
}
