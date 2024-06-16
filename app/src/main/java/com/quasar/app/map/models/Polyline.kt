package com.quasar.app.map.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Point

@Entity
data class Polyline(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val positions: List<Position>
    ) {
    fun points(): List<Point> {
        return positions.map { it.toPoint() }
    }
}