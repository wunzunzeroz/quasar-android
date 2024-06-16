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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Position

@Composable
fun ViewPolylineSheet(
    polyline: Polyline, onDelete: (Polyline) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(polyline.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(polyline.distance(), style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // TODO - Add confirm dialog
        OutlinedButton(onClick = { onDelete(polyline) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Delete")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun ViewPolylineSheetPreview() {
    ViewPolylineSheet(Polyline(name = "Test line", positions = listOf()), onDelete = {})
}