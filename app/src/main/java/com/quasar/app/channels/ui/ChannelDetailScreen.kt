package com.quasar.app.channels.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quasar.app.channels.models.Channel
import com.quasar.app.map.components.BottomNav
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelDetailScreen(
    navController: NavHostController,
    channelId: String,
    channelViewModel: ChannelsViewModel = get(),
) {
    var channel by remember { mutableStateOf<Channel?>(null) }

    LaunchedEffect(channelId) {
        channel = channelViewModel.getChannel(channelId)
    }

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
                Spacer(modifier = Modifier.height(8.dp))
                Text(channel.name, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(channel.description, style = MaterialTheme.typography.bodyMedium)
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