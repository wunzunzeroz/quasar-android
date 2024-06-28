package com.quasar.app.map.models

data class Speed(val value: Double, val unit: SpeedUnit) {
    fun toMetresPerSecond(): Double {
        return when (unit) {
            SpeedUnit.Mps -> value.toDouble()
            SpeedUnit.Kts -> value * 0.514444
            SpeedUnit.Kmh -> value * 0.277778
        }
    }
}

enum class SpeedUnit {
    Mps, // Metres per second
    Kts, // Knots
    Kmh, // Kilometres per hour
}
