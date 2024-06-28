package com.quasar.app.map.models

data class Speed(val value: Double, val unit: SpeedUnit) {
    fun toMetresPerSecond(): Double {
        return when (unit) {
            SpeedUnit.Mps -> value.toDouble()
            SpeedUnit.Kts -> value * 0.514444
            SpeedUnit.Kmh -> value * 0.277778
        }
    }

    override fun toString(): String {
        return "$value ${getUnitAbbrev()}"
    }

    private fun getUnitAbbrev(): String {
        return when (unit) {
            SpeedUnit.Mps -> "m/s"
            SpeedUnit.Kts -> "knots"
            SpeedUnit.Kmh -> "kmh"
        }
    }
}

enum class SpeedUnit {
    Mps, // Metres per second
    Kts, // Knots
    Kmh, // Kilometres per hour
}
