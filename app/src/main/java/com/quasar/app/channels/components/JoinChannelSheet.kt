package com.quasar.app.channels.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JoinChannelSheet(onJoin: (String) -> Unit, modifier: Modifier = Modifier) {
    var id by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Text("Join Channel")
        Divider()

        Spacer(modifier = Modifier.height(32.dp))

        Text("Enter the ID of the channel you wish to join")
        TextField(value = id, onValueChange = { id = it }, label = { Text("Channel ID") })

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            onJoin(id)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Join Channel")
        }

    }
}