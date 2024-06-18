package com.quasar.app.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quasar.app.map.ui.CoordinateType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinateTypeSelector(
    value: CoordinateType, onSelect: (CoordinateType) -> Unit, modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        FilterChip(selected = value == CoordinateType.LatLngDec,
            onClick = { onSelect(CoordinateType.LatLngDec) },
            label = { Text("Lat/Lng DD", style = MaterialTheme.typography.labelSmall) })

        Spacer(modifier = Modifier.width(8.dp))

        FilterChip(selected = value == CoordinateType.LatLngDms,
            onClick = { onSelect(CoordinateType.LatLngDms) },
            label = { Text("Lat/Lng DMS", style = MaterialTheme.typography.labelSmall) })

        Spacer(modifier = Modifier.width(8.dp))

        FilterChip(selected = value == CoordinateType.GridRef,
            onClick = { onSelect(CoordinateType.GridRef) },
            label = { Text("Grid Ref", style = MaterialTheme.typography.labelSmall) })
    }
}