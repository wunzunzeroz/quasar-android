package com.quasar.app.map.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.TextButton
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quasar.app.R
import com.quasar.app.map.styles.MapStyle

@Composable
fun SelectMapStyleSheet(onMapStyleSelected: (MapStyle) -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(stringResource(R.string.select_map_style_sheet_title))
        Divider()
        Spacer(modifier = Modifier.height(4.dp))
        StyleButton(icon = Icons.Filled.Hiking,
            label = "Outdoors",
            onClick = { onMapStyleSelected(MapStyle.Outdoors) })
        StyleButton(icon = Icons.Filled.Satellite,
            label = "Satellite",
            onClick = { onMapStyleSelected(MapStyle.Satellite) })
        StyleButton(icon = Icons.Filled.Terrain,
            label = "Topographic",
            onClick = { onMapStyleSelected(MapStyle.Topographic) })
        StyleButton(icon = Icons.Filled.DirectionsBoat,
            label = "Marine",
            onClick = { onMapStyleSelected(MapStyle.Nautical) })
        StyleButton(icon = Icons.Filled.Flight,
            label = "Aviation",
            onClick = { onMapStyleSelected(MapStyle.Aeronautical) })
    }
}

@Composable
private fun StyleButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, "")
            Text(label)
        }
    }
}
