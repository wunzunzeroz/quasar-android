package com.quasar.app.map.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.DistanceUnit
import com.quasar.app.map.utils.Utils

@Composable
fun AddCircleSheet(
    center: Point, onSave: (Circle) -> Unit, modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var radius by remember { mutableDoubleStateOf(0.0) }
    var units by remember { mutableStateOf(DistanceUnit.Metres) }
    var color by remember { mutableStateOf(Color.Magenta) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Add Circle", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

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
            value = Utils.RoundNumberToDp(radius, 1).toString(),
            onValueChange = { radius = it.toDouble() },
            label = { Text("Radius") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        var expanded by remember { mutableStateOf(false) }

        TextField(
            value = units.name,
            readOnly = true,  // Make the text field read-only
            onValueChange = {},
            label = { Text("Units") },
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { expanded = true })
            })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
//                modifier = Modifier.fillMaxWidth()
        ) {
            DistanceUnit.entries.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        units = item
                    },
                    text = {
                        Text(item.name)
                    })
            }
        }
        ColorSelectDropdown(initialValue = color, onValueChange = { color = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val circle = Circle(
                name = name,
                center = Position.fromPoint(center),
                radius = radius,
                distanceUnit = units,
                color = Utils.convertColorToHexString(color)
            )
            onSave(circle)
        }) {
            Text(text = "Save Circle")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
@Preview(showSystemUi = true)
fun AddCircleSheetPreview() {
    AddCircleSheet(center = Point.fromLngLat(174.0, -36.0), onSave = {})
}
