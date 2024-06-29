package com.quasar.app.map.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quasar.app.map.utils.Utils
import com.quasar.app.ui.theme.NeonOrange

@Entity(tableName = "search_pattern")
data class SearchPattern(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val datum: Position,
    val legs: List<SearchLeg>,
    val color: String = Utils.convertColorToHexString(NeonOrange)
) {
    val waypoints: List<Position>
        get() {
            val result = mutableListOf(datum)
            result.addAll(legs.map { it.endPoint })
            return result
        }

    companion object {
        fun createCreepingLineSearch(
            startPoint: Position,
            trackDirection: Heading,
            speed: Speed,
            sweepWidth: Distance,
            legCount: Int,
            legDistance: Distance
        ): SearchPattern {
            var legHeading = trackDirection.subtract(90)
            var legStartPoint = startPoint

            val searchLegs = mutableListOf<SearchLeg>()

            for (i in 1..(legCount + 1)) {

                val distance = if (i == 1) {
                    legDistance / 2
                } else if (isExtensionLeg(i)) {
                    sweepWidth
                } else {
                    legDistance
                }

                val endPoint = legStartPoint.move(legHeading, distance)
                val time = distance.timeToCover(speed)

                val leg = SearchLeg(legStartPoint, endPoint, legHeading, speed, distance, time)
                searchLegs.add(leg)

                val nextHeading =
                    if (isRightTurningLeg(i)) legHeading.add(90) else legHeading.subtract(90)
                legHeading = nextHeading
                legStartPoint = endPoint
            }

            return SearchPattern(datum = startPoint, legs = searchLegs)
        }

        private fun isExtensionLeg(index: Int): Boolean {
            // Every second leg will be an extension leg
            return index % 2 == 0;
        }

        private fun isRightTurningLeg(index: Int): Boolean {
            // The legs will go RR LL RR LL etc
            return ((index - 1) / 2) % 2 == 0
        }
    }
}