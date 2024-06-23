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
import com.quasar.app.channels.components.CreateChannelSheet
import com.quasar.app.map.components.BottomNav
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(navController: NavHostController, channelViewModel: ChannelsViewModel = get()) {
//    val channels by channelViewModel.channels.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by channelViewModel.uiState.collectAsState()
    val channels by channelViewModel.chnls.collectAsStateWithLifecycle(emptyList())

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
            Button(onClick = {
                channelViewModel.showCreateChannelSheet()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Create Channel")
            }
            LazyColumn {
                items(channels, key = { it.name }) { channel ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(channel.name)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(channel.description)
                    }
                }
            }
        }

    }
}