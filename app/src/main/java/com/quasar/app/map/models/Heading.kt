package com.quasar.app.map.models

class Heading(value: Int, val type: HeadingType = HeadingType.True) {
    var value: Int = value
        set(value) {
            require(value in 0..360) { "Heading must be between 0 and 360." }
            field = value
        }
        get() {
            return if (field == 0) {
                360
            } else {
                field
            }
        }

    init {
        require(value in 0..360) { "Heading must be between 0 and 360." }
        this.value = value
    }

    operator fun minus(degrees: Int): Heading {
        val newValue = value - degrees

        return if (newValue < 0) {
            Heading(newValue + 360, type)
        } else {
            Heading(newValue, type)
        }
    }

    operator fun plus(degrees: Int): Heading {
        val newValue = value + degrees

        return if (newValue > 360) {
            Heading(newValue - 360, type)
        } else {
            Heading(newValue, type)
        }
    }

    override fun toString(): String {
        return "$value° ${type.name}"
    }
}

enum class HeadingType {
    True,
    Magnetic
}
