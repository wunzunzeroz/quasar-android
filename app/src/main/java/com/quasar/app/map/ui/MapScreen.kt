package com.quasar.app.map.ui

import android.animation.ValueAnimator
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pentagon
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.common.location.LocationError
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon as GeoJsonPolygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.turf.TurfMeasurement
import com.quasar.app.QuasarScreen
import com.quasar.app.R
import com.quasar.app.channels.models.ChannelMember
import com.quasar.app.map.components.AddAnnotationSheet
import com.quasar.app.map.components.AddCircleSheet
import com.quasar.app.map.components.AddPolygonSheet
import com.quasar.app.map.components.AddWaypointSheet
import com.quasar.app.map.components.BottomNav
import com.quasar.app.map.components.LocationDetailSheet
import com.quasar.app.map.components.PermissionRequest
import com.quasar.app.map.components.AddPolylineSheet
import com.quasar.app.map.components.AnnotationConfirmation
import com.quasar.app.map.components.GoToLocationSheet
import com.quasar.app.map.components.SelectMapStyleSheet
import com.quasar.app.map.components.ViewCircleSheet
import com.quasar.app.map.components.ViewPolygonSheet
import com.quasar.app.map.components.ViewPolylineSheet
import com.quasar.app.map.components.ViewWaypointDetailSheet
import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.styles.StyleLoader
import com.quasar.app.map.utils.Utils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.WaypointMarkerType

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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Map Data", modifier = Modifier.padding(16.dp))

                    IconButton(onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }, modifier = Modifier.padding(horizontal = 8.dp)) {
                        Icon(Icons.Filled.ArrowBack, "Close drawer")
                    }
                }
                Divider()
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column {
                        NavigationDrawerItem(label = { Text("Waypoints") },
                            icon = { Icon(Icons.Filled.Place, "") },
                            selected = false,
                            onClick = { navController.navigate(QuasarScreen.WaypointsScreen.name) })
                        NavigationDrawerItem(label = { Text("Polylines") },
                            icon = { Icon(Icons.Filled.Timeline, "") },
                            selected = false,
                            onClick = { navController.navigate(QuasarScreen.PolylinesScreen.name) })
                        NavigationDrawerItem(label = { Text("Polygons") },
                            icon = { Icon(Icons.Filled.Pentagon, "") },
                            selected = false,
                            onClick = { navController.navigate(QuasarScreen.PolygonsScreen.name) })
                        NavigationDrawerItem(label = { Text("Circles") },
                            icon = { Icon(Icons.Filled.Circle, "") },
                            selected = false,
                            onClick = { navController.navigate(QuasarScreen.CirclesScreen.name) })
                    }
                    NavigationDrawerItem(icon = { Icon(Icons.Filled.ExitToApp, "") },
                        label = { Text(text = stringResource(id = R.string.log_out)) },
                        selected = false,
                        onClick = {
                            AuthUI.getInstance().signOut(ctx).addOnCompleteListener {
                                navController.navigate(QuasarScreen.LandingScreen.name)
                            }
                        })
                }
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
                BottomNav(navController)
            }) { contentPadding ->
                val tappedLocation = remember { mutableStateOf(Point.fromLngLat(0.0, 0.0)) }
                var longTappedLocation by remember { mutableStateOf(Point.fromLngLat(0.0, 0.0)) }
                val userLocation by viewModel.userLocation.collectAsState()

                val mapViewportState = remember {
                    MapViewportState().apply {
                        setCameraOptions {
                            zoom(11.0)
                            center(userLocation)
                            pitch(0.0)
                            bearing(0.0)
                        }
                    }
                }

                var activeWaypoint: Waypoint? by remember { mutableStateOf(null) }
                var activePolyline: Polyline? by remember { mutableStateOf(null) }
                var activePolygon: Polygon? by remember { mutableStateOf(null) }
                var activeCircle: Circle? by remember { mutableStateOf(null) }

                val waypoints by viewModel.waypoints.collectAsState()
                val polylines by viewModel.polylines.collectAsState()
                val polygons by viewModel.polygons.collectAsState()
                val circles by viewModel.circles.collectAsState()

                val lastLocations by viewModel.lastLocations.collectAsStateWithLifecycle(listOf())

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

                            BottomSheetContentType.GoToLocation -> GoToLocationSheet(onSubmit = {
                                tappedLocation.value = it.toPoint()
                                mapViewportState.flyTo(cameraOptions = cameraOptions {
                                    center(it.toPoint())
                                    zoom(12.0)
                                }, MapAnimationOptions.mapAnimationOptions { duration(3000) })
                                viewModel.setBottomSheetVisible(false)
                            })

                            BottomSheetContentType.ViewLocationDetail -> LocationDetailSheet(
                                userLocation,
                                tappedLocation.value,
                                {
                                    viewModel.setBottomSheetContentType(BottomSheetContentType.AddWaypoint)
                                })

                            BottomSheetContentType.AddWaypoint -> AddWaypointSheet(
                                tappedLocation.value,
                                onCreateWaypoint = {
                                    coroutineScope.launch {
                                        viewModel.saveWaypoint(it)
                                        viewModel.setBottomSheetVisible(false)
                                        tappedLocation.value = null
                                    }
                                })

                            BottomSheetContentType.ViewWaypointDetail -> activeWaypoint?.let {
                                ViewWaypointDetailSheet(it, onUpdateWaypoint = { wpt ->
                                    coroutineScope.launch {
                                        viewModel.updateWaypoint(wpt)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                }, onDeleteWaypoint = { wpt ->
                                    coroutineScope.launch {
                                        viewModel.deleteWaypoint(wpt)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                })
                            }

                            BottomSheetContentType.AddAnnotation -> AddAnnotationSheet(onAddPolyline = {
                                viewModel.setLongTapActionType(LongTapAction.AddPointToPolyline)
                                viewModel.setBottomSheetVisible(false)
                                viewModel.addPointToPolyCandidate(longTappedLocation)
                            }, onAddPolygon = {
                                viewModel.setLongTapActionType(LongTapAction.AddPointToPolygon)
                                viewModel.setBottomSheetVisible(false)
                                viewModel.addPointToPolyCandidate(longTappedLocation)
                            }, onAddCircle = {
                                viewModel.addPointToPolyCandidate(longTappedLocation)
                                viewModel.setBottomSheetContentType(BottomSheetContentType.AddCircleAnnotation)
                            })

                            BottomSheetContentType.AddCircleAnnotation -> AddCircleSheet(
                                longTappedLocation,
                                onSave = { circle ->
                                    coroutineScope.launch {
                                        viewModel.saveCircle(circle)
                                        viewModel.setBottomSheetVisible(false)
                                        viewModel.setLongTapActionType(LongTapAction.ShowAnnotationMenu)
                                        viewModel.clearPolyCandidate()
                                    }
                                })

                            BottomSheetContentType.AddPolylineAnnotation -> AddPolylineSheet(points = uiState.polyCandidate,
                                onSave = { polyline ->
                                    coroutineScope.launch {
                                        viewModel.savePolyline(polyline)
                                        viewModel.setBottomSheetVisible(false)
                                        viewModel.setLongTapActionType(LongTapAction.ShowAnnotationMenu)
                                        viewModel.clearPolyCandidate()
                                    }
                                })

                            BottomSheetContentType.AddPolygonAnnotation -> AddPolygonSheet(points = uiState.polyCandidate,
                                onSave = { polygon ->
                                    coroutineScope.launch {
                                        viewModel.savePolygon(polygon)
                                        viewModel.setBottomSheetVisible(false)
                                        viewModel.setLongTapActionType(LongTapAction.ShowAnnotationMenu)
                                        viewModel.clearPolyCandidate()
                                    }
                                })

                            BottomSheetContentType.ViewPolylineDetail -> activePolyline?.let {
                                ViewPolylineSheet(polyline = it, onDelete = {
                                    coroutineScope.launch {
                                        viewModel.deletePolyline(it)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                })
                            }

                            BottomSheetContentType.ViewPolygonDetail -> activePolygon?.let { polygon ->
                                ViewPolygonSheet(polygon = polygon, onDelete = {
                                    coroutineScope.launch {
                                        viewModel.deletePolygon(it)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                })
                            }

                            BottomSheetContentType.ViewCircleDetail -> activeCircle?.let { circle ->
                                ViewCircleSheet(circle = circle, onDelete = {
                                    coroutineScope.launch {
                                        viewModel.deleteCircle(it)
                                        viewModel.setBottomSheetVisible(false)
                                    }
                                })
                            }
                        }
                    }
                }

                if (!locationPermissionState.status.isGranted) {
                    return@Scaffold PermissionRequest(onLaunchPermissionRequest = { locationPermissionState.launchPermissionRequest() })
                }

                // Map Container
                Box(modifier = Modifier.fillMaxSize()) {
                    val hapticFeedback = LocalHapticFeedback.current

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
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            longTappedLocation = it

                            when (uiState.longTapAction) {
                                LongTapAction.ShowAnnotationMenu -> {
                                    viewModel.setBottomSheetContentType(BottomSheetContentType.AddAnnotation)
                                    viewModel.setBottomSheetVisible(true)
                                }

                                LongTapAction.AddPointToPolyline -> viewModel.addPointToPolyCandidate(
                                    it
                                )

                                LongTapAction.AddPointToPolygon -> viewModel.addPointToPolyCandidate(
                                    it
                                )
                            }

                            true
                        },
                        style = { MapStyle(style = Style.SATELLITE_STREETS) },
                        modifier = Modifier.fillMaxSize(),
                    ) {
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

                        MapWaypoints(waypoints, onWaypointClicked = {
                            activeWaypoint = it
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewWaypointDetail)
                            viewModel.setBottomSheetVisible(true)
                        })


                        // Candidate:
                        if (uiState.longTapAction == LongTapAction.AddPointToPolyline) {
                            MapPolyline(uiState.polyCandidate)
                        }
                        if (uiState.longTapAction == LongTapAction.AddPointToPolygon) {
                            MapPolygon(uiState.polyCandidate)
                        }

                        // From Repo:
                        MapPolylines(polylines, onLineClicked = {
                            activePolyline = it
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewPolylineDetail)
                            viewModel.setBottomSheetVisible(true)
                        })
                        MapPolygons(polygons, onLineClicked = {
                            activePolygon = it
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewPolygonDetail)
                            viewModel.setBottomSheetVisible(true)
                        })
                        MapCircles(circles, uiState.mapStyle, onCircleClicked = {
                            activeCircle = it
                            viewModel.setBottomSheetContentType(BottomSheetContentType.ViewCircleDetail)
                            viewModel.setBottomSheetVisible(true)
                        })
                        LastLocations(lastLocations)
                    } // End MapboxMap

                    MapUiOverlay(onLayerButtonClick = {
                        viewModel.setBottomSheetContentType(BottomSheetContentType.SelectMapStyle)
                        viewModel.setBottomSheetVisible(true)
                    }, onLocationButtonClick = {
                        mapViewportState.flyTo(cameraOptions = cameraOptions {
                            center(userLocation)
                            zoom(12.0)
                        }, MapAnimationOptions.mapAnimationOptions { duration(3000) })
                    }, mapRotationEnabled = mapRotationEnabled.value, onRotateButtonClick = {
                        mapRotationEnabled.value = !mapRotationEnabled.value
                        mapGesturesSettings.value = mapGesturesSettings.value.toBuilder()
                            .setRotateEnabled(mapRotationEnabled.value).build()
                        Log.d(logTag, "Map rotation enabled: $mapRotationEnabled")
                    }, onGotoButtonClick = {
                        viewModel.setBottomSheetContentType(BottomSheetContentType.GoToLocation)
                        viewModel.setBottomSheetVisible(true)

                    }, modifier = Modifier.padding(contentPadding)
                    )

                    if (uiState.polyCandidate.isNotEmpty()) {
                        AnnotationConfirmation(data = if (uiState.longTapAction == LongTapAction.AddPointToPolyline) getDistance(
                            uiState.polyCandidate
                        ) else getArea(uiState.polyCandidate), onConfirm = {
                            if (uiState.longTapAction == LongTapAction.AddPointToPolyline) {
                                viewModel.setBottomSheetContentType(BottomSheetContentType.AddPolylineAnnotation)
                            } else {
                                viewModel.setBottomSheetContentType(BottomSheetContentType.AddPolygonAnnotation)
                            }
                            viewModel.setBottomSheetVisible(true)
                        }, onUndo = {
                            viewModel.undoPolyLineCandidate()
                        }, onCancel = {
                            viewModel.clearPolyCandidate()
                            viewModel.setLongTapActionType(LongTapAction.ShowAnnotationMenu)
                        }, modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }

    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun LastLocations(lastLocations: List<ChannelMember>) {
    val ll = lastLocations.filter { it.lastLocation != null }

    val waypointAnnotations = ll.map {
        PointAnnotationOptions().withPoint(Point.fromLngLat(it.lastLocation!!.longitude, it.lastLocation.latitude))
            .withIconImage(getMarkerBitmap(LocalContext.current, WaypointMarkerType.Person))
    }

    val circleAnnotations = ll.map {
        CircleAnnotationOptions().withPoint(Point.fromLngLat(it.lastLocation!!.longitude, it.lastLocation.latitude))
            .withCircleColor(Color.Magenta.toArgb()).withCircleRadius(10.0).withCircleOpacity(0.7)
    }

    CircleAnnotationGroup(annotations = circleAnnotations, onClick = {
        true
    })

    PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
        true
    })
}

fun getArea(points: List<Point>): String {
    val polygon = GeoJsonPolygon.fromLngLats(listOf(points))
    val area = TurfMeasurement.area(polygon)


    if (area > 10_000) {
        val km = Utils.RoundNumberToDp(area / 1_000_000, 1)

        return "$km sq KM"
    }

    val m = Utils.RoundNumberToDp(area, 1)

    return "$m sq M"
}

fun getDistance(polyline: List<Point>): String {
    if (polyline.isEmpty()) {
        return ""
    }

    val lineString = LineString.fromLngLats(polyline)
    val length = TurfMeasurement.length(lineString, "kilometers")

    val km = Utils.RoundNumberToDp(length, 1)

    if (km > 1) {
        return "$km KM"
    }

    return "${km * 1000} M"
}
