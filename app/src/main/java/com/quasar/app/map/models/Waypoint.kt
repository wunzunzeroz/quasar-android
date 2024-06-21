package com.quasar.app.map.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quasar.app.map.utils.Utils

@Entity
data class Waypoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val position: Position,
    val name: String,
    val code: String,
    val markerType: WaypointMarkerType,
    val color: String,
) {
    fun getColor(): Color {
        return Color(color.toColorInt())
    }

    override fun toString(): String {
        return "Waypoint {Name = $name, Position = ${position.latLngDecimal}, Code = $code, MarkerType = $markerType, MarkerColor = $color}"
    }

    fun withPosition(position: Position): Waypoint {
        return Waypoint(id, position, name, code, markerType, color)
    }

    fun withName(name: String): Waypoint {
        return Waypoint(id, position, name, code, markerType, color)
    }

    fun withCode(code: String): Waypoint {
        return Waypoint(id, position, name, code, markerType, color)
    }

    fun withMarkerType(markerType: WaypointMarkerType): Waypoint {
        return Waypoint(id, position, name, code, markerType, color)
    }

    fun withColor(color: Color): Waypoint {
        return Waypoint(id, position, name, code, markerType, Utils.convertColorToHexString(color))
    }
}

data class CreateWaypointInput(
    val position: Position,
    val name: String,
    val code: String,
    val markerType: WaypointMarkerType,
    val markerColor: Long,
)

enum class WaypointMarkerType() {
    Flag,
    Marker,
    Pin,
    Cross,

    Circle,
    Triangle,
    Square,
    Star,

    QuestionMark,
    ExclamationPoint,
    CheckMark,
    CrossMark,

    Car,
    Boat,
    Plane,
    Helicopter,

    Forest,
    Water,
    Mountain,
    Beach,

    Fire,
    Anchor,
    Lifering,
    Target,

    Tent,
    House,
    Building,
    Castle,

    Footprints,
    Person,
    People,
    Skull,

    Drinks,
    Food,
    WaterSource,
    Fuel
}
