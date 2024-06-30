package com.quasar.app.map.models

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Assert.assertTrue

class SearchPatternTest {

    @Test
    fun creepingLineSearch() {
        val legDistance = Distance(500.0, DistanceUnit.Metres)

        val searchPattern = SearchPattern.createCreepingLineSearch(
            startPoint = Position(0.0, 0.0),
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

    @Test
    fun sectorSearch() {
        val searchPattern = SearchPattern.createSectorSearch(
            datum = Position(0.0, 0.0),
            initialLegDirection = Heading(0),
            speed = Speed(10.0, SpeedUnit.Kts),
            legCount = 8,
            sweepWidth = Distance(100.0, DistanceUnit.Metres)
        )

        val legs = searchPattern.legs
        val firstLeg = legs.first()
        val secondLeg = legs[1]
        val thirdLeg = legs[2]
        val fourthLeg = legs[3]

        assertEquals(Distance(300.0, DistanceUnit.Metres), firstLeg.distance)

        println("DONE")
    }
}