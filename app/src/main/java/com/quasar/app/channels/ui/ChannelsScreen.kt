package com.quasar.app.channels.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.quasar.app.QuasarScreen
import com.quasar.app.channels.components.ChannelRow
import com.quasar.app.channels.components.CreateChannelSheet
import com.quasar.app.channels.components.JoinChannelSheet
import com.quasar.app.map.components.BottomNav
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(navController: NavHostController, channelViewModel: ChannelsViewModel = get()) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by channelViewModel.uiState.collectAsState()
    val channels by channelViewModel.channels.collectAsStateWithLifecycle(emptyList())

    val members by channelViewModel.members.collectAsStateWithLifecycle(emptyList())

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Channels") }, navigationIcon = {
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
        if (uiState.bottomSheetVisible) {
            ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {
                when (uiState.bottomSheetType) {
                    BottomSheetContentType.CreateChannel -> CreateChannelSheet(onCreate = {
                        coroutineScope.launch {
                            val channelId = channelViewModel.createChannel(it)
                            Toast.makeText(
                                context, "Channel created with id $channelId", Toast.LENGTH_LONG
                            ).show()
                            channelViewModel.hideBottomSheet()
                        }
                    })

                    BottomSheetContentType.JoinChannel -> JoinChannelSheet(onJoin = {
                        coroutineScope.launch {

                            channelViewModel.joinChannel(it)
                            channelViewModel.hideBottomSheet()
                        }
                    })
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = {
                    channelViewModel.showJoinChannelSheet()
                }, modifier = Modifier.weight(1f)) {
                    Text("Join Channel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    channelViewModel.showCreateChannelSheet()
                }, modifier = Modifier.weight(1f)) {
                    Text("Create Channel")
                }
            }
            if (channels.isEmpty()) {
                Text("You haven't joined any channels yet")
            } else {
                LazyColumn {
                    items(channels, key = { it.name }) { channel ->
                        ChannelRow(channel, onChannelSelect = {
                            navController.navigate("${QuasarScreen.ChannelDetailScreen.name}/${channel.id}")
                        })
                    }
                }

                Text("All Members")
                LazyColumn {
                    items(members) {member ->
                        Text(member.name)
                    }
                }
            }
        }

    }
}