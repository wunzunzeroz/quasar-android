package com.quasar.app.map.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Undo
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.common.location.LocationError
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.turf.TurfMeasurement
import com.quasar.app.QuasarScreen
import com.quasar.app.R
import com.quasar.app.map.components.AddAnnotationSheet
import com.quasar.app.map.components.AddWaypointSheet
import com.quasar.app.map.components.BottomBar
import com.quasar.app.map.components.LocationDetailSheet
import com.quasar.app.map.components.MapActionButton
import com.quasar.app.map.components.PermissionRequest
import com.quasar.app.map.components.SavePolylineSheet
import com.quasar.app.map.components.SelectMapStyleSheet
import com.quasar.app.map.components.ViewPolylineSheet
import com.quasar.app.map.components.ViewWaypointDetailSheet
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.models.WaypointMarkerType
import com.quasar.app.map.styles.StyleLoader
import com.quasar.app.map.utils.Utils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

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
                var longTappedLocation by remember { mutableStateOf(Point.fromLngLat(0.0, 0.0)) }
                val userLocation by viewModel.userLocation.collectAsState()

                var activeWaypoint: Waypoint? by remember { mutableStateOf(null) }
                var activePolyline: Polyline? by remember { mutableStateOf(null) }

                val waypoints by viewModel.waypoints.collectAsState()
                val polylines by viewModel.polylines.collectAsState()

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
                            }, onAddCircle = { /*TODO*/ })

                            BottomSheetContentType.AddCircleAnnotation -> TODO()
                            BottomSheetContentType.AddPolylineAnnotation -> SavePolylineSheet(
                                points = uiState.polyCandidate,
                                onSave = { polyline ->
                                    coroutineScope.launch {
                                        viewModel.savePolyline(polyline)
                                        viewModel.setBottomSheetVisible(false)
                                        viewModel.clearPolylineCandidate()
                                    }
                                })

                            BottomSheetContentType.AddPolygonAnnotation -> TODO()
                            BottomSheetContentType.ViewPolylineDetail -> activePolyline?.let {
                                ViewPolylineSheet(polyline = it, onDelete = {
                                    coroutineScope.launch {
                                        viewModel.deletePolyline(it)
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

                                LongTapAction.CreateCircleAtPoint -> TODO()
                                LongTapAction.AddPointToPolygon -> viewModel.addPointToPolyCandidate(
                                    it
                                )
                            }

                            true
                        },
                        style = { MapStyle(style = Style.SATELLITE_STREETS) },
                        modifier = Modifier.fillMaxSize(),
                    ) {

                        // TODO - Extract these into composables


                        val waypointAnnotations = waypoints.map {
                            Log.d(
                                logTag,
                                "Waypoint: ${it.name} - ${it.position.gridReference}, ${it.position.latLngDecimal}, color: ${it.getColor()}"
                            )
                            PointAnnotationOptions().withPoint(it.position.toPoint())
                                .withIconImage(getMarkerBitmap(ctx, it.markerType))
                        }

                        PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
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
                    }
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
                    }, modifier = Modifier.padding(contentPadding)
                    )

                    if (uiState.polyCandidate.isNotEmpty()) {
                        AnnotationConfirmation(
                            data = if (uiState.longTapAction == LongTapAction.AddPointToPolyline) getDistance(
                                uiState.polyCandidate
                            ) else getArea(uiState.polyCandidate),
                            onConfirm = {
                                viewModel.setBottomSheetContentType(BottomSheetContentType.AddPolylineAnnotation)
                                viewModel.setBottomSheetVisible(true)
                            },
                            onUndo = {
                                viewModel.undoPolyLineCandidate()
                            },
                            onCancel = {
                                // TODO - Create polymorphic annotation handler
                                viewModel.clearPolylineCandidate()
                                viewModel.setLongTapActionType(LongTapAction.ShowAnnotationMenu)
                            },
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }

    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolylines(polylines: List<Polyline>, onLineClicked: (Polyline) -> Unit) {
    val lines = polylines.map {
        PolylineAnnotationOptions().withPoints(it.points()).withLineColor(Color.Cyan.toArgb())
            .withLineWidth(3.0)
    }

    val allPoints = mutableListOf<Point>()
    polylines.forEach { allPoints.addAll(it.points()) }

    val points = allPoints.map {
        CircleAnnotationOptions().withPoint(it).withCircleRadius(6.0)
            .withCircleColor(Color.Cyan.toArgb()).withDraggable(false) // TODO - Support draggables
    }

    PolylineAnnotationGroup(annotations = lines, onClick = {
        val line = polylines.first { line -> line.points() == it.points }
        onLineClicked(line)

        true
    })
    CircleAnnotationGroup(annotations = points, onClick = {
        val line = polylines.first { line -> line.points().contains(it.point) }
        onLineClicked(line)

        true
    })
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolyline(polyline: List<Point>, color: Color = Color.Magenta) {
    Log.d("MapScreen", "Point count: ${polyline.count()}")

    val polylinePoints = polyline.map {
        CircleAnnotationOptions().withPoint(it).withCircleRadius(4.0)
            .withCircleColor(color.toArgb())
            .withCircleOpacity(0.7)
    }
    PolylineAnnotation(
        polyline, lineWidth = 3.0, lineColorInt = color.toArgb(), lineOpacity = 0.5
    )
    CircleAnnotationGroup(annotations = polylinePoints)
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapPolygon(points: List<Point>, color: Color = Color.Magenta) {
    Log.d("MapScreen", "Point count: ${points.count()}")

    val polylinePoints = points.map {
        CircleAnnotationOptions().withPoint(it).withCircleRadius(4.0)
            .withCircleColor(color.toArgb())
            .withCircleOpacity(0.7)
    }
    PolygonAnnotation(
        points = listOf(points), fillColorInt = color.toArgb(), fillOpacity = 0.5
    )
    CircleAnnotationGroup(annotations = polylinePoints)
}

fun getArea(points: List<Point>): String {
    val polygon = Polygon.fromLngLats(listOf(points))
    val area = TurfMeasurement.area(polygon)


    if (area > 1000) {
        val km = Utils.RoundNumberToDp(area / 1000, 1)

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

@Composable
fun MapUiOverlay(
    onLayerButtonClick: () -> Unit,
    onLocationButtonClick: () -> Unit,
    mapRotationEnabled: Boolean,
    onRotateButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        MapActionButton(icon = Icons.Filled.Layers, onClick = onLayerButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        MapActionButton(icon = Icons.Filled.MyLocation, onClick = onLocationButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        MapActionButton(
            icon = if (mapRotationEnabled) Icons.Filled.Navigation else Icons.Filled.RotateLeft,
            onClick = onRotateButtonClick
        )
    }
}

@Composable
fun AnnotationConfirmation(
    data: String,
    onConfirm: () -> Unit,
    onUndo: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(data, modifier = Modifier.padding(8.dp))
            }
            Row {
                MapActionButton(icon = Icons.Filled.Cancel, onClick = onCancel)
                Spacer(modifier = Modifier.width(8.dp))
                MapActionButton(icon = Icons.Filled.Undo, onClick = onUndo)
                Spacer(modifier = Modifier.width(8.dp))
                MapActionButton(icon = Icons.Filled.Check, onClick = onConfirm)
            }
        }
    }

}

private fun getMarkerBitmap(context: Context, markerType: WaypointMarkerType): Bitmap {
    val drawableId = getDrawableForWaypointMarker(markerType)
    val drawable = ContextCompat.getDrawable(context, drawableId)

    if (drawable is BitmapDrawable) {
        return resizeBitmap(drawable.bitmap)
    }
    throw Exception("Unable to get bitmap for marker type: $markerType")
}

private fun resizeBitmap(bitmap: Bitmap, size: Int = 40): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, size, size, false)
}

fun getDrawableForWaypointMarker(markerType: WaypointMarkerType): Int {
    return when (markerType) {
        WaypointMarkerType.Flag -> R.drawable.flag
        WaypointMarkerType.Marker -> R.drawable.marker
        WaypointMarkerType.Pin -> R.drawable.marker
        WaypointMarkerType.Cross -> R.drawable.cross
        WaypointMarkerType.Circle -> R.drawable.check_mark
        WaypointMarkerType.Triangle -> R.drawable.triangle
        WaypointMarkerType.Square -> R.drawable.marker
        WaypointMarkerType.Star -> R.drawable.star
        WaypointMarkerType.QuestionMark -> R.drawable.question_mark
        WaypointMarkerType.ExclamationPoint -> R.drawable.exclamation_mark
        WaypointMarkerType.CheckMark -> R.drawable.check_mark
        WaypointMarkerType.CrossMark -> R.drawable.cross_mark
        WaypointMarkerType.Car -> R.drawable.car
        WaypointMarkerType.Boat -> R.drawable.boat
        WaypointMarkerType.Plane -> R.drawable.plane
        WaypointMarkerType.Helicopter -> R.drawable.helicopter
        WaypointMarkerType.Forest -> R.drawable.forest
        WaypointMarkerType.Water -> R.drawable.water
        WaypointMarkerType.Mountain -> R.drawable.mountains
        WaypointMarkerType.Beach -> R.drawable.beach
        WaypointMarkerType.Fire -> R.drawable.fire
        WaypointMarkerType.Anchor -> R.drawable.anchor
        WaypointMarkerType.Lifering -> R.drawable.lifering
        WaypointMarkerType.Target -> R.drawable.target
        WaypointMarkerType.Tent -> R.drawable.tent
        WaypointMarkerType.House -> R.drawable.house
        WaypointMarkerType.Building -> R.drawable.building
        WaypointMarkerType.Castle -> R.drawable.castle
        WaypointMarkerType.Footprints -> R.drawable.footsteps
        WaypointMarkerType.Person -> R.drawable.person
        WaypointMarkerType.People -> R.drawable.people
        WaypointMarkerType.Skull -> R.drawable.skull
        WaypointMarkerType.Drinks -> R.drawable.drinks
        WaypointMarkerType.Food -> R.drawable.food
        WaypointMarkerType.WaterSource -> R.drawable.water_source
        WaypointMarkerType.Fuel -> R.drawable.fuel
    }
}
