package com.quasar.app.map.models

import com.mapbox.turf.TurfConstants

data class Distance(val value: Double, val unit: DistanceUnit) {
    fun toTurfUnit(): String {
        return when (unit) {
            DistanceUnit.Metres -> TurfConstants.UNIT_METRES
            DistanceUnit.Kilometres -> TurfConstants.UNIT_KILOMETRES
            DistanceUnit.NauticalMiles -> TurfConstants.UNIT_NAUTICAL_MILES
        }
    }

    override fun toString(): String {
        return "$value ${getUnitAbbrev()}"
    }

    private fun getUnitAbbrev(): String {
        return when (unit) {
            DistanceUnit.Metres -> "m"
            DistanceUnit.Kilometres -> "km"
            DistanceUnit.NauticalMiles -> "nm"
        }
    }

    /**
     * @param[speed] The speed at which to cover distance
     * @return Time in seconds to cover distance at given speed
     */
    fun timeToCover(speed: Speed): Int {
        return when (unit) {
            DistanceUnit.Metres -> (value / speed.toMetresPerSecond()).toInt()
            DistanceUnit.Kilometres -> ((value * 1000) / speed.toMetresPerSecond()).toInt()
            DistanceUnit.NauticalMiles -> ((value * 1852) / speed.toMetresPerSecond()).toInt()
        }
    }

    operator fun div(i: Int): Distance {
        return Distance(value / i, unit)
    }

    operator fun times(i: Int): Distance {
        return Distance(value * i, unit)
    }
}
