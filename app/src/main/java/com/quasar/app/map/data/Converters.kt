package com.quasar.app.map.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.SearchLeg

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

    @TypeConverter
    fun fromSearchLegs(legs: List<SearchLeg>): String {
        return Gson().toJson(legs)
    }

    @TypeConverter
    fun toSearchLegs(legsString: String): List<SearchLeg> {
        val listType = object : TypeToken<List<SearchLeg>>() {}.type
        return Gson().fromJson(legsString, listType)
    }

    @TypeConverter
    fun fromSearchLeg(leg: SearchLeg): String {
        return leg.toJson()
    }

    @TypeConverter
    fun toSearchLeg(json: String): SearchLeg {
        return SearchLeg.fromJson(json)
    }
}
