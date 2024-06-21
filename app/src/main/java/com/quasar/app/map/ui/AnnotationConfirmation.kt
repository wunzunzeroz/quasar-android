package com.quasar.app.map.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quasar.app.map.components.MapActionButton

@Composable
fun AnnotationConfirmation(
    data: String,
    onConfirm: () -> Unit,
    onUndo: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(data, modifier = Modifier.padding(8.dp))
            }
            Row {
                MapActionButton(icon = Icons.Filled.Cancel, onClick = onCancel)
                Spacer(modifier = Modifier.width(8.dp))
                MapActionButton(icon = Icons.Filled.Undo, onClick = onUndo)
                Spacer(modifier = Modifier.width(8.dp))
                MapActionButton(icon = Icons.Filled.Check, onClick = onConfirm)
            }
        }
    }

}