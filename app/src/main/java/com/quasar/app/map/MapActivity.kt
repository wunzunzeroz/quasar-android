package com.quasar.app.map

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.quasar.app.LaunchActivity
import com.quasar.app.R
import com.quasar.app.ui.theme.QUASARTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QUASARTheme {
                MapScreen(logout = { logout() })
            }
        }
    }

    private fun logout() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            val intent = Intent(this, LaunchActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, MapboxExperimental::class)
@Composable
fun MapScreen(logout: () -> Unit, model: MapViewModel = get(), modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet {
            Text("QUASAR", modifier = Modifier.padding(16.dp))
            Divider()
            NavigationDrawerItem(icon = { Icon(Icons.Filled.ExitToApp, "") },
                label = { Text(text = stringResource(id = R.string.log_out)) },
                selected = false,
                onClick = { logout() })
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    QUASARTheme {
        MapScreen(logout = { /*TODO*/ })
    }
}