package com.quasar.app.map.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quasar.app.map.components.MapActionButton

@Composable
fun MapUiOverlay(
    onLayerButtonClick: () -> Unit,
    onLocationButtonClick: () -> Unit,
    mapRotationEnabled: Boolean,
    onRotateButtonClick: () -> Unit,
    onGotoButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        MapActionButton(icon = Icons.Filled.Layers, onClick = onLayerButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        MapActionButton(icon = Icons.Filled.MyLocation, onClick = onLocationButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        MapActionButton(
            icon = if (mapRotationEnabled) Icons.Filled.Navigation else Icons.Filled.RotateLeft,
            onClick = onRotateButtonClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        MapActionButton(
            icon = Icons.Filled.NearMe,
            onClick = onGotoButtonClick
        )
    }
}