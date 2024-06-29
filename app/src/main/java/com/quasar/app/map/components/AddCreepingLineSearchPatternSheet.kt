package com.quasar.app.map.components

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.quasar.app.map.models.Distance
import com.quasar.app.map.models.DistanceUnit
import com.quasar.app.map.models.Heading
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.SearchPattern
import com.quasar.app.map.models.Speed
import com.quasar.app.map.models.SpeedUnit

@Composable
fun AddCreepingLineSearchPatternSheet(
    datum: Point,
    onCreatePattern: (SearchPattern) -> Unit,
    modifier: Modifier = Modifier
) {
    var trackDirection by remember { mutableIntStateOf(360) }
    var speed by remember { mutableIntStateOf(10) }
    var sweepWidth by remember { mutableIntStateOf(100) }
    var legCount by remember { mutableIntStateOf(11) }
    var legDistance by remember { mutableIntStateOf(500) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text("Create Search", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = "$trackDirection",
                onValueChange = { trackDirection = it.toInt() },
                label = { Text("Track Direction") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = "$speed knots",
                onValueChange = { speed = it.toInt() },
                label = { Text("Speed") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = "$sweepWidth m",
                onValueChange = { sweepWidth = it.toInt() },
                label = { Text("Sweep Width") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = legCount.toString(),
                onValueChange = { legCount = it.toInt() },
                label = { Text("Leg Count") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = "$legDistance m",
                onValueChange = { legDistance = it.toInt() },
                label = { Text("Leg Distance") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val search = SearchPattern.CreateCreepingLineSearch(
                startPoint = Position.fromPoint(datum),
                trackDirection = Heading(trackDirection),
                speed = Speed(speed.toDouble(), SpeedUnit.Kts),
                sweepWidth = Distance(sweepWidth.toDouble(), DistanceUnit.Metres),
                legCount = legCount,
                legDistance = Distance(legDistance.toDouble(), DistanceUnit.Metres)
            )

            onCreatePattern(search)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Search")
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun AddCreepingLineSearchPatternSheetPreview() {
    AddCreepingLineSearchPatternSheet(datum = Point.fromLngLat(0.0, 0.0), onCreatePattern = {})
}