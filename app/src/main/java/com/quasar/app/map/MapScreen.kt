package com.quasar.app.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.quasar.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MapScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = false, drawerContent = {
            ModalDrawerSheet {
                Text("QUASAR", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(icon = { Icon(Icons.Filled.ExitToApp, "") },
                    label = { Text(text = stringResource(id = R.string.log_out)) },
                    selected = false,
                    onClick = { /* TODO */ })
            }
        }) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(text = "QUASAR") }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                })
            }) { contentPadding ->
                MapboxMap(
                    Modifier.fillMaxSize(),
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(11.0)
                            center(Point.fromLngLat(174.831123, -36.833331))
                            pitch(0.0)
                            bearing(0.0)
                        }
                    },
                    style = { MapStyle(style = Style.OUTDOORS) }
                ) {
                    MapEffect(Unit) { mapView ->
                        // Use mapView to access all the Mapbox Maps APIs including plugins etc.
                        // For example, to enable debug mode:
                        mapView.mapboxMap.loadStyle("")
                    }
                }
            }
        }

    }
}