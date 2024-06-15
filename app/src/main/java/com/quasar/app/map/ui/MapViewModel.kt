package com.quasar.app.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.WaypointsRepository
import com.quasar.app.map.models.CreateWaypointInput
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.styles.MapStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MapViewModel(
    private val waypointsRepository: WaypointsRepository,
    private val circlesRepository: CirclesRepository,
    private val polylinesRepository: PolylinesRepository,
    private val polygonsRepository: PolygonsRepository,
    private val sketchRepository: SketchRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _userLocation = MutableStateFlow(Point.fromLngLat(174.858, -36.787))
    val userlocation: StateFlow<Point> = _userLocation.asStateFlow()

    val waypoints: StateFlow<List<Waypoint>> =
        waypointsRepository.getAllWaypoints().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    fun setMapStyle(style: MapStyle) {
        _uiState.update { currentState ->
            currentState.copy(
                mapStyle = style
            )
        }
    }

    fun setBottomSheetVisible(visible: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheetVisible = visible
            )
        }
    }

    fun setBottomSheetContentType(type: BottomSheetContentType) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheetType = type
            )
        }
    }

    fun setLongTapActionType(type: LongTapAction) {
        _uiState.update { currentState ->
            currentState.copy(
                longTapAction = type
            )
        }
    }

    fun addPointToPolylineCandidate(point: Point) {
        _uiState.update { currentState ->
            val newList = mutableListOf<Point>()
            newList.addAll(currentState.polylineCandidate)
            newList.add(point)

            currentState.copy(
                polylineCandidate = newList
            )
        }
    }

    fun savePolylineCandidate() {
        TODO()
    }

    fun clearPolylineCandidate() {
        _uiState.update { currentState ->
            currentState.copy(
                polylineCandidate = listOf()
            )
        }
    }

    fun setUserLocation(point: Point) {
        _userLocation.update { point }
    }

    suspend fun saveWaypoint(input: CreateWaypointInput) {
        // TODO - Fix the ID thing
        val waypoint =
            Waypoint(0, input.position, input.name, input.code, input.markerType, input.markerColor)

        waypointsRepository.insertWaypoint(waypoint)
    }

    suspend fun updateWaypoint(waypoint: Waypoint) {
        waypointsRepository.updateWaypoint(waypoint)
    }

    suspend fun deleteWaypoint(waypoint: Waypoint) {
        waypointsRepository.deleteWaypoint(waypoint)
    }
}
