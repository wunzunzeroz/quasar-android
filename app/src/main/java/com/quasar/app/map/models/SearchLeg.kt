package com.quasar.app.map.models

import com.google.gson.Gson

data class SearchLeg(
    val startPoint: Position,
    val endPoint: Position,
    val heading: Heading,
    val speed: Speed,
    val distance: Distance,
    val time: Int
) {
    override fun toString(): String {
        return "LEG: Start Point: {${startPoint.latLngDecimal}}, End Point: {${endPoint.latLngDecimal}}, Heading: {${heading}}, Speed: {${speed.value} ${speed.unit.name}}, Distance: {${distance.value} ${distance.unit.name}}, Time: {$time sec}"
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): SearchLeg {
            return Gson().fromJson(json, SearchLeg::class.java)

        }
    }
}
