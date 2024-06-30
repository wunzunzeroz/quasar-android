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
fun AddAnnotationSheet(
    onAddPolyline: () -> Unit,
    onAddPolygon: () -> Unit,
    onAddCircle: () -> Unit,
    onAddCreepingLineSearch: () -> Unit,
    onAddSectorSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Add Annotation", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onAddPolyline, modifier = Modifier.fillMaxWidth()) {
            Text("Add Polyline")
        }
        OutlinedButton(onClick = onAddPolygon, modifier = Modifier.fillMaxWidth()) {
            Text("Add Polygon")
        }
        OutlinedButton(onClick = onAddCircle, modifier = Modifier.fillMaxWidth()) {
            Text("Add Circle")
        }
        Divider()
        OutlinedButton(onClick = onAddCreepingLineSearch, modifier = Modifier.fillMaxWidth()) {
            Text("Add Creeping Line Search")
        }
        OutlinedButton(onClick = onAddSectorSearch, modifier = Modifier.fillMaxWidth()) {
            Text("Add Sector Search")
        }


        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Preview(showSystemUi = true)
@Composable
fun AddAnnotationSheetPreview() {
    AddAnnotationSheet(onAddPolyline = {}, onAddPolygon = {}, onAddCircle = {}, {}, {})
}