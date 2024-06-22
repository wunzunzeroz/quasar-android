package com.quasar.app.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quasar.app.map.models.Position
import com.quasar.app.map.ui.CoordinateType

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

