package com.quasar.app.map.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Waypoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val markerType: WaypointMarkerType,
    val markerColor: Long,
    val position: Position,
    val description: String
) {
    fun getColor(): Color {
        return Color(markerColor.toInt()) // Convert Long back to Color
    }
}

data class CreateWaypointInput(
    val position: Position,
    val name: String,
    val code: String,
    val markerType: WaypointMarkerType,
    val markerColor: Long,
)

enum class WaypointMarkerType {
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
