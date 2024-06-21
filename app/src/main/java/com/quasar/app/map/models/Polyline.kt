package com.quasar.app.map.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement
import com.quasar.app.map.utils.Utils

@Entity
data class Polyline(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val positions: List<Position>,
    val color: String
) {
    fun getColor(): Color {
        return Color(color.toColorInt())
    }

    fun points(): List<Point> {
        return positions.map { it.toPoint() }
    }

    fun distance(): String {
        if (positions.isEmpty()) {
            return ""
        }

        val lineString = LineString.fromLngLats(points())
        val length = TurfMeasurement.length(lineString, "kilometers")

        val km = Utils.RoundNumberToDp(length, 1)

        if (km > 1) {
            return "$km KM"
        }

        return "${km * 1000} M"
    }
}