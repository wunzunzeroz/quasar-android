package com.quasar.app.map.models

import org.junit.Test
import org.junit.Assert.assertTrue

class CreepingLineSearchPatternTest {

    @Test
    fun getLegs() {
        val legDistance = Distance(500.0, DistanceUnit.Metres)

        val searchPattern = CreepingLineSearchPattern(
            datum = Position(0.0, 0.0),
            trackDirection = Heading(0),
            speed = Speed(10.0, SpeedUnit.Kts),
            legCount = 8,
            legDistance = legDistance,
            sweepWidth = Distance(100.0, DistanceUnit.Metres)
        )

        val legs = searchPattern.legs
        val firstLeg = legs.first()
        val secondLeg = legs[1]
        val thirdLeg = legs[2]
        val fourthLeg = legs[3]

        assertTrue(firstLeg.distance.value == (legDistance / 2).value)

        println("DONE")
    }
}