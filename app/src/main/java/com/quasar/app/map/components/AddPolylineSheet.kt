package com.quasar.app.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Position
import com.quasar.app.map.utils.Utils

@Composable
fun AddPolylineSheet(
    points: List<Point>, onSave: (Polyline) -> Unit, modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Magenta) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Save Polyline", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        ColorSelectDropdown(initialValue = color, onValueChange = { color = it })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val polyline = Polyline(
                name = name,
                positions = points.map { Position.fromPoint(it) },
                color = Utils.convertColorToHexString(color)
            )
            onSave(polyline)
        }) {
            Text(text = "Save Polyline")
        }


        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun SavePolylineSheetPreview() {
    AddPolylineSheet(listOf(), {})
}