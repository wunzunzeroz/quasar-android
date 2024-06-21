package com.quasar.app.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.WaypointsRepository
import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.CreateWaypointInput
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.Polyline
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
    val userLocation: StateFlow<Point> = _userLocation.asStateFlow()

    val waypoints: StateFlow<List<Waypoint>> =
        waypointsRepository.getAllWaypoints().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val polylines: StateFlow<List<Polyline>> =
        polylinesRepository.getAll().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val polygons: StateFlow<List<Polygon>> =
        polygonsRepository.getAll().map { it }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val circles: StateFlow<List<Circle>> =
        circlesRepository.getAll().map { it }.stateIn(
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

    fun addPointToPolyCandidate(point: Point) {
        _uiState.update { currentState ->
            val newList = mutableListOf<Point>()
            newList.addAll(currentState.polyCandidate)
            newList.add(point)

            currentState.copy(
                polyCandidate = newList
            )
        }
    }

    fun undoPolyLineCandidate() {
        _uiState.update { cur ->
            cur.copy(
                polyCandidate = cur.polyCandidate.dropLast(1)
            )
        }
    }

    fun clearPolyCandidate() {
        _uiState.update { currentState ->
            currentState.copy(
                polyCandidate = listOf()
            )
        }
    }

    fun setUserLocation(point: Point) {
        _userLocation.update { point }
    }

    suspend fun saveWaypoint(waypoint: Waypoint) {
        waypointsRepository.insertWaypoint(waypoint)
    }

    suspend fun updateWaypoint(waypoint: Waypoint) {
        waypointsRepository.updateWaypoint(waypoint)
    }

    suspend fun deleteWaypoint(waypoint: Waypoint) {
        waypointsRepository.deleteWaypoint(waypoint)
    }

    suspend fun savePolyline(polyline: Polyline) {
        polylinesRepository.insert(polyline)
    }

    suspend fun deletePolyline(polyline: Polyline) {
        polylinesRepository.delete(polyline)
    }

    suspend fun savePolygon(polygon: Polygon) {
        polygonsRepository.insert(polygon)
    }

    suspend fun deletePolygon(polygon: Polygon) {
        polygonsRepository.delete(polygon)
    }

    suspend fun saveCircle(circle: Circle) {
        circlesRepository.insert(circle)
    }

    suspend fun updateCircle(circle: Circle) {
        circlesRepository.update(circle)
    }

    suspend fun deleteCircle(circle: Circle) {
        circlesRepository.delete(circle)
    }
}
