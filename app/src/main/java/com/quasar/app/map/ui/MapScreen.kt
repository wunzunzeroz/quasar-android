package com.quasar.app.map.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.quasar.app.QuasarScreen
import com.quasar.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MapScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val logTag = "MapScreen"
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val ctx = LocalContext.current

        ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = false, drawerContent = {
            ModalDrawerSheet {
                Text("QUASAR", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(icon = { Icon(Icons.Filled.ExitToApp, "") },
                    label = { Text(text = stringResource(id = R.string.log_out)) },
                    selected = false,
                    onClick = {
                        AuthUI.getInstance().signOut(ctx).addOnCompleteListener {
                            navController.navigate(QuasarScreen.LandingScreen.name)
                        }
                    })
            }
        }) {
            Scaffold(topBar = {
                // Maybe we don't need the top bar and drawer? Just a search box instead?
                TopAppBar(title = { Text(text = "QUASAR") }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
                )
            }, bottomBar = {
                BottomBar()
            }) { contentPadding ->
                MapboxMap(
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(11.0)
                            center(Point.fromLngLat(174.831123, -36.833331))
                            pitch(0.0)
                            bearing(0.0)
                        }
                    },
                    onMapClickListener = {
                        Log.d(
                            logTag, "User tapped map at Lat/Lng: ${it.latitude()}/${it.longitude()}"
                        )

                        true
                    },
                    onMapLongClickListener = {
                        Log.d(
                            logTag,
                            "User long tapped map at Lat/Lng: ${it.latitude()}/${it.longitude()}"
                        )

                        true
                    },
                    style = { MapStyle(style = Style.OUTDOORS) },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MapEffect(Unit) { mapView ->
                        // Use mapView to access all the Mapbox Maps APIs including plugins etc.
                        // For example, to enable debug mode:
//                        mapView.mapboxMap.loadStyle(Style.OUTDOORS)
                    }
                }
            }
        }

    }
}

@Composable
fun BottomBar() {
    NavigationBar() {
        val selectedItem = remember {
            mutableIntStateOf(0)
        }

        val items = listOf(
            "Map",
            "Tasks",
            "Chat",
            "Teams",
            "Profile"
        )
        val icons = listOf(
            Icons.Default.Map,
            Icons.Default.List,
            Icons.Default.Chat,
            Icons.Default.Group,
            Icons.Default.Person
        )
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem.intValue == index,
                onClick = { selectedItem.intValue = index },
            )
        }
    }
}