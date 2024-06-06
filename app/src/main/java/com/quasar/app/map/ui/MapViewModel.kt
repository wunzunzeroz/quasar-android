package com.quasar.app.map.ui

import androidx.lifecycle.ViewModel
import com.mapbox.geojson.Point
import com.quasar.app.map.data.CirclesRepository
import com.quasar.app.map.data.PolygonsRepository
import com.quasar.app.map.data.PolylinesRepository
import com.quasar.app.map.data.SketchRepository
import com.quasar.app.map.data.WaypointsRepository
import com.quasar.app.map.styles.MapStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun setUserLocation(point: Point) {
        _userLocation.update { point }
    }
}
