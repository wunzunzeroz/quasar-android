package com.quasar.app.channels.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.quasar.app.map.components.BottomNav
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelDetailScreen(
    navController: NavHostController,
    channelId: String,
    channelViewModel: ChannelsViewModel = get(),
) {
    val channel by channelViewModel.getChannel(channelId)
        .collectAsStateWithLifecycle(initialValue = null)

    Scaffold(topBar = {
        TopAppBar(title = { Text("Channel Details") }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        )
    }, bottomBar = { BottomNav(navHostController = navController) }) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            channel?.let { channel ->
                // TODO - Improve UI on this page
                // - Add Posts
                // - Add leave channel button
                Spacer(modifier = Modifier.height(8.dp))
                Text(channel.name, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(channel.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                ChannelIdRow(channel.id)
                Spacer(modifier = Modifier.height(32.dp))

                Text("Members:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(channel.members) { member ->
                        Text(member.name)
                    }
                }
            } ?: Text("Loading...")
        }
    }
}

@Composable
fun ChannelIdRow(channelId: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text("Channel ID:")
            Spacer(modifier = Modifier.width(8.dp))
            Text(channelId, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = {
            val channelIdString = "Join my channel in QUASAR: '$channelId'"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Channel ID")
                putExtra(Intent.EXTRA_TEXT, channelIdString)
            }

            context.startActivity(Intent.createChooser(intent, "Share Channel ID"))
        }) {
            Icon(Icons.Filled.Share, "Share Channel ID")
        }
    }
}