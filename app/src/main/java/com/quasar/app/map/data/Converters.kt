package com.quasar.app.map.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.room.TypeConverter
import com.quasar.app.map.models.Position

class Converters {
    @TypeConverter
    fun positionToString(pos: Position): String {
        return "${pos.latLngDecimal.latitude},${pos.latLngDecimal.longitude}"
    }

    @TypeConverter
    fun stringToPosition(str: String): Position {
        val latLng = str.split(",")
        val lat = latLng.first()
        val lng = latLng.last()

        return Position(lat.toDouble(), lng.toDouble())
    }

    @TypeConverter
    fun positionListToStringList(positions: List<Position>): String {
        val strings = positions.map { positionToString(it) }
        return strings.joinToString("|")
    }

    @TypeConverter
    fun stringListToPositionList(str: String): List<Position> {
       val positions = str.split("|").map { stringToPosition(it) }

        return positions
    }
}
