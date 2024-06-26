package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.quasar.app.map.models.Waypoint
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaypointsScreen(navController: NavHostController, viewModel: MapViewModel = get()) {
    val waypoints by viewModel.waypoints.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Waypoints") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            LazyColumn {
                items(waypoints) { waypoint ->
                    WaypointRow(waypoint = waypoint, onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteWaypoint(it)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun WaypointRow(waypoint: Waypoint, onDelete: (Waypoint) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = waypoint.name, modifier = modifier.padding(8.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(waypoint.position.gridReference.toString())
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onDelete(waypoint) }) {
                // TODO - Add confirmation dialog
                Icon(Icons.Filled.Delete, contentDescription = "Delete waypoint")

            }
        }
    }
}
