package com.quasar.app.map.models

class CreepingLineSearchPattern(
    datum: Position,
    trackDirection: Heading,
    speed: Speed,
    sweepWidth: Distance,
    legCount: Int,
    legDistance: Distance
) {
    val legs: List<SearchLeg>

    val waypoints: List<Position>
        get() = legs.map { it.startPoint }

    init {
        var legHeading = trackDirection.subtract(90)
        var legStartPoint = datum

        val searchLegs = mutableListOf<SearchLeg>()

        for (i in 1..legCount) {

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

        legs = searchLegs
    }

    private fun isExtensionLeg(index: Int): Boolean {
        // Every second leg will be an extension leg
        return index % 2 == 0;
    }

    private fun isRightTurningLeg(index: Int): Boolean {
        // The legs will go RR LL RR LL etc
//        return (index / 2) % 2 == 0
        return ((index - 1) / 2) % 2 == 0
    }
}