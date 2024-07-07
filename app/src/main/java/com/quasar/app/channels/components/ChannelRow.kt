package com.quasar.app.channels.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quasar.app.domain.models.Channel

@Composable
fun ChannelRow(
    channel: Channel,
    onChannelSelect: (Channel) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp)
            .padding(start = 16.dp, end = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(channel.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Groups, "Member number")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    channel.memberCount.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        IconButton(onClick = { onChannelSelect(channel) }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChannelRowPreview() {
    Column {
        ChannelRow(
            Channel(
                "channel-a",
                "Channel A",
                "Desc A",
                2,
                listOf()
            ),
            {}
        )
        ChannelRow(
            Channel(
                "channel-b",
                "Channel B",
                "Desc B",
                3,
                listOf()
            ),
            {}
        )
        ChannelRow(
            Channel(
                "channel-c",
                "Channel C",
                "Desc C",
                1,
                listOf()
            ),
            {}
        )
    }
}