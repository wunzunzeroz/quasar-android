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
import com.quasar.app.channels.models.CreateChannelInput

@Composable
fun CreateChannelSheet(onCreate: (CreateChannelInput) -> Unit, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Text("Create Channel")
        Divider()

        Spacer(modifier = Modifier.height(32.dp))

        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = description,
            onValueChange = { description = it },
            label = { Text("Description") })

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val channel = CreateChannelInput(name = name, description = description)

            onCreate(channel)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Channel")
        }

    }
}