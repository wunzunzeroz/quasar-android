package com.quasar.app.map.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quasar.app.map.components.DistanceUnit

@Entity
class Circle (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val center: Position,
    val radius: Double,
    val distanceUnit: DistanceUnit
)