package com.quasar.app.map.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quasar.app.R
import com.quasar.app.map.styles.MapStyle

@Composable
fun SelectMapStyleSheet(onMapStyleSelected: (MapStyle) -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(horizontal = 16.dp)) {
        Text(stringResource(R.string.select_map_style_sheet_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(12.dp))
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
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth() ) {
            Icon(icon, "")
            Spacer(modifier = Modifier.width(8.dp))
            Text(label)
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun SelectMapStyleSheetPreview() {
    SelectMapStyleSheet(onMapStyleSelected = {})
}