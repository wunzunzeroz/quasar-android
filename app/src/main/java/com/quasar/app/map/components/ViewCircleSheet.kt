package com.quasar.app.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.utils.Utils

@Composable
fun ViewCircleSheet(
    circle: Circle, onDelete: (Circle) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(circle.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                Utils.RoundNumberToDp(circle.radius, 1).toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = circle.distanceUnit.name)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // TODO - Add confirm dialog
        OutlinedButton(onClick = { onDelete(circle) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Delete")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}