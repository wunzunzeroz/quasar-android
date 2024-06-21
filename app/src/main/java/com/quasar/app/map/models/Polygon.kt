package com.quasar.app.map.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.turf.TurfMeasurement
import com.quasar.app.map.utils.Utils

@Entity
data class Polygon(
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

    fun area(): String {
        val polygon = Polygon.fromLngLats(listOf(points()))
        val area = TurfMeasurement.area(polygon)

        if (area > 10_000) {
            val km = Utils.RoundNumberToDp(area / 1_000_000, 1)

            return "$km sq KM"
        }

        val m = Utils.RoundNumberToDp(area, 1)

        return "$m sq M"
    }
}