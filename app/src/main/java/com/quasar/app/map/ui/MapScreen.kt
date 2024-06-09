package com.quasar.app.map.ui

import android.animation.ValueAnimator
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.JsonElement
import com.mapbox.common.location.LocationError
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.DefaultSettingsProvider
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.quasar.app.QuasarScreen
import com.quasar.app.R
import com.quasar.app.map.components.AddWaypointSheet
import com.quasar.app.map.components.BottomBar
import com.quasar.app.map.components.LocationDetailSheet
import com.quasar.app.map.components.MapActionButton
import com.quasar.app.map.components.PermissionRequest
import com.quasar.app.map.components.SelectMapStyleSheet
import com.quasar.app.map.components.ViewWaypointDetailSheet
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.styles.StyleLoader
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.math.log

@OptIn(
    ExperimentalMaterial3Api::class, MapboxExperimental::class, ExperimentalPermissionsApi::class
)
@Composable
fun MapScreen(navController: NavHostController, viewModel: MapViewModel = get()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val uiState by viewModel.uiState.collectAsState()

        val logTag = "MapScreen"
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val ctx = LocalContext.current

        val locationPermissionState =
            rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

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
                val tappedLocation = remember { mutableStateOf(Point.fromLngLat(0.0, 0.0)) }
                val userLocation by viewModel.userlocation.collectAsState()
                var activeWaypoint: Waypoint? by remember { mutableStateOf(null) }

                val waypoints by viewModel.waypoints.collectAsState()

                val coroutineScope = rememberCoroutineScope()


                if (uiState.bottomSheetVisible) {
                    ModalBottomSheet(onDismissRequest = {
                        viewModel.setBottomSheetVisible(false)
                        tappedLocation.value = null
                    }) {
                        when (uiState.bottomSheetType) {
                            BottomSheetContentType.SelectMapStyle -> SelectMapStyleSheet(
                                onMapStyleSelected = {
                                    viewModel.setMapStyle(it)
                                    viewModel.setBottomSheetVisible(false)
                                })

                            BottomSheetContentType.GoToLocation -> TODO()
                            BottomSheetContentType.ViewLocationDetail -> LocationDetailSheet(
                                userLocation,
                                tappedLocation.value,
                                {
                                    viewModel.setBottomSheetContentType(BottomSheetContentType.AddWaypoint)
                                })

                            BottomSheetContentType.AddWaypoint -> AddWaypointSheet(tappedLocation.value,
                                onCreateWaypoint = {
                                    coroutineScope.launch {
                                        viewModel.saveWaypoint(it)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                })

                            BottomSheetContentType.ViewWaypointDetail -> activeWaypoint?.let {
                                ViewWaypointDetailSheet(it)
                            }

                            BottomSheetContentType.AddAnnotation -> TODO()
                            BottomSheetContentType.AddCircleAnnotation -> TODO()
                            BottomSheetContentType.AddPolylineAnnotation -> TODO()
                            BottomSheetContentType.AddPolygonAnnotation -> TODO()
                        }
                    }
                }

                if (!locationPermissionState.status.isGranted) {
                    return@Scaffold PermissionRequest(onLaunchPermissionRequest = { locationPermissionState.launchPermissionRequest() })
                }

                // Map Container
                Box(modifier = Modifier.fillMaxSize()) {
                    val mapViewportState = remember {
                        MapViewportState().apply {
                            setCameraOptions {
                                zoom(11.0)
                                center(Point.fromLngLat(174.831123, -36.833331))
                                pitch(0.0)
                                bearing(0.0)
                            }
                        }
                    }

                    var mapRotationEnabled = remember {
                        mutableStateOf(false)
                    }
                    val initialGestures = GesturesSettings {
                        rotateEnabled = false
                    }
                    val mapGesturesSettings = remember {
                        mutableStateOf(GesturesSettings {
                            rotateEnabled = mapRotationEnabled.value
                        })
                    }

                    MapboxMap(
                        mapViewportState = mapViewportState,
                        gesturesSettings = mapGesturesSettings.value,
                        compass = {
                            Compass(
                                modifier = Modifier.padding(contentPadding),
                                alignment = Alignment.TopEnd
                            )
                        },
                        scaleBar = {
                            ScaleBar(
                                modifier = Modifier.padding(contentPadding),
                                alignment = Alignment.BottomStart
                            )
                        },
                        onMapClickListener = {
                            Log.d(
                                logTag,
                                "User tapped map at Lat/Lng: ${it.latitude()}/${it.longitude()}"
                            )
                            tappedLocation.value = it
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewLocationDetail)
                            viewModel.setBottomSheetVisible(true)

                            true
                        },
                        onMapLongClickListener = {
                            Log.d(
                                logTag,
                                "User long tapped map at Lat/Lng: ${it.latitude()}/${it.longitude()}"
                            )

                            true
                        },
                        style = { MapStyle(style = Style.SATELLITE_STREETS) },
                        modifier = Modifier.fillMaxSize(),
                    ) {

                        val waypointAnnotations = waypoints.map {
                            Log.d(
                                logTag,
                                "Waypoint: ${it.name} - ${it.position.gridReference}, ${it.position.latLngDecimal}, color: ${it.getColor()}"
                            )
                            CircleAnnotationOptions().withPoint(it.position.toPoint())
                                .withCircleRadius(10.0)
//                                .withCircleColor(Color.Red.toArgb())
                                .withCircleColor("#FF4f00")

                        }

                        CircleAnnotationGroup(annotations = waypointAnnotations, onClick = {
                            val wpt = waypoints.first { w -> w.position.toPoint() == it.point }
                            Log.d(logTag, "Waypoint tapped: ${wpt.name}")

                            activeWaypoint = wpt
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewWaypointDetail)
                            viewModel.setBottomSheetVisible(true)

                            true
                        })

                        if (tappedLocation.value != null) {
                            CircleAnnotation(
                                point = tappedLocation.value,
                                circleColorInt = MaterialTheme.colorScheme.primary.toArgb(),
                                circleStrokeColorInt = Color.Black.toArgb(), // TODO
                                circleRadius = 6.0,
                                circleStrokeWidth = 3.0,
                            )
                        }

                        MapEffect { mapView ->
                            mapView.location.let {
                                it.enabled = true
                                it.pulsingEnabled = true
                                it.puckBearing = PuckBearing.HEADING
                                it.pulsingEnabled = true
                                it.locationPuck = createDefault2DPuck(withBearing = true)
                            }

                        }
                        MapEffect { mapView ->
                            // TODO - Use location service instead of location provider
                            mapView.location.getLocationProvider().let {
                                it?.registerLocationConsumer(
                                    // TODO - Extract consumer to own file
                                    object : LocationConsumer {
                                        override fun onBearingUpdated(
                                            vararg bearing: Double,
                                            options: (ValueAnimator.() -> Unit)?
                                        ) {
                                        }

                                        override fun onError(error: LocationError) {
                                        }

                                        override fun onHorizontalAccuracyRadiusUpdated(
                                            vararg radius: Double,
                                            options: (ValueAnimator.() -> Unit)?
                                        ) {
                                        }

                                        override fun onLocationUpdated(
                                            vararg location: Point,
                                            options: (ValueAnimator.() -> Unit)?
                                        ) {
                                            Log.d(
                                                logTag, "User location updated to lat/lng: ${
                                                    location.first().latitude()
                                                }/${location.first().longitude()}"
                                            )
                                            location.first()
                                                .let { point -> viewModel.setUserLocation(point) }

                                        }

                                        override fun onPuckAccuracyRadiusAnimatorDefaultOptionsUpdated(
                                            options: ValueAnimator.() -> Unit
                                        ) {
                                        }

                                        override fun onPuckBearingAnimatorDefaultOptionsUpdated(
                                            options: ValueAnimator.() -> Unit
                                        ) {
                                        }

                                        override fun onPuckLocationAnimatorDefaultOptionsUpdated(
                                            options: ValueAnimator.() -> Unit
                                        ) {
                                        }
                                    })
                            }

                        }
                        MapEffect(uiState.mapStyle) { mapView ->
                            val style = StyleLoader.getStyle(uiState.mapStyle, ctx)
                            Log.d(logTag, "Set map style: ${uiState.mapStyle}")
                            mapView.mapboxMap.loadStyle(style)
                        }

                    }
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(horizontal = 8.dp)
                    ) {
                        MapActionButton(icon = Icons.Filled.Layers, onClick = {
                            viewModel.setBottomSheetContentType(BottomSheetContentType.SelectMapStyle)
                            viewModel.setBottomSheetVisible(true)
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                        MapActionButton(icon = Icons.Filled.MyLocation, onClick = {
                            mapViewportState.flyTo(cameraOptions = cameraOptions {
                                center(userLocation)
                                zoom(12.0)
                            }, MapAnimationOptions.mapAnimationOptions { duration(3000) })
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                        MapActionButton(icon = if (mapRotationEnabled.value) Icons.Filled.Navigation else Icons.Filled.RotateLeft,
                            onClick = {
                                mapRotationEnabled.value = !mapRotationEnabled.value
                                mapGesturesSettings.value = mapGesturesSettings.value.toBuilder()
                                    .setRotateEnabled(mapRotationEnabled.value).build()
                                Log.d(logTag, "Map rotation enabled: $mapRotationEnabled")
                            })
                    }
                }
            }
        }

    }
}
