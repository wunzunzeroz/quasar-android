package com.quasar.app.map.models

import org.junit.Assert.assertEquals
import org.junit.Test


class SearchLegTest {
    @Test
    fun toJson() {
        val expectedResult = "{\"startPoint\":{\"latLngDecimal\":{\"latitude\":-36.123,\"longitude\":174.456},\"gridReference\":{\"eastings\":1731026,\"northings\":6001427},\"latLngDegreesMinutes\":{\"latitude\":{\"degrees\":36,\"minutes\":7.38,\"cardinalDirection\":\"S\"},\"longitude\":{\"degrees\":174,\"minutes\":27.36,\"cardinalDirection\":\"E\"}}},\"endPoint\":{\"latLngDecimal\":{\"latitude\":-37.123,\"longitude\":175.456},\"gridReference\":{\"eastings\":1818191,\"northings\":5888659},\"latLngDegreesMinutes\":{\"latitude\":{\"degrees\":37,\"minutes\":7.38,\"cardinalDirection\":\"S\"},\"longitude\":{\"degrees\":175,\"minutes\":27.36,\"cardinalDirection\":\"E\"}}},\"heading\":{\"type\":\"True\",\"value\":90},\"speed\":{\"value\":10.0,\"unit\":\"Kts\"},\"distance\":{\"value\":500.0,\"unit\":\"Metres\"},\"time\":30}"
        val sut = SearchLeg(
            Position(-36.123, 174.456),
            Position(-37.123, 175.456),
            Heading(90),
            Speed(10.0, SpeedUnit.Kts),
            Distance(500.0, DistanceUnit.Metres),
            30
        )

        val result = sut.toJson()

        assertEquals(expectedResult, result)
    }


    @Test
    fun fromJson() {
        val json = "{\"startPoint\":{\"latLngDecimal\":{\"latitude\":-36.123,\"longitude\":174.456},\"gridReference\":{\"eastings\":1731026,\"northings\":6001427},\"latLngDegreesMinutes\":{\"latitude\":{\"degrees\":36,\"minutes\":7.38,\"cardinalDirection\":\"S\"},\"longitude\":{\"degrees\":174,\"minutes\":27.36,\"cardinalDirection\":\"E\"}}},\"endPoint\":{\"latLngDecimal\":{\"latitude\":-37.123,\"longitude\":175.456},\"gridReference\":{\"eastings\":1818191,\"northings\":5888659},\"latLngDegreesMinutes\":{\"latitude\":{\"degrees\":37,\"minutes\":7.38,\"cardinalDirection\":\"S\"},\"longitude\":{\"degrees\":175,\"minutes\":27.36,\"cardinalDirection\":\"E\"}}},\"heading\":{\"type\":\"True\",\"value\":90},\"speed\":{\"value\":10.0,\"unit\":\"Kts\"},\"distance\":{\"value\":500.0,\"unit\":\"Metres\"},\"time\":30}"

        val result = SearchLeg.fromJson(json)

        assertEquals(-36.123, result.startPoint.latLngDecimal.latitude, 0.0)
        assertEquals(174.456, result.startPoint.latLngDecimal.longitude, 0.0)

        assertEquals(-37.123, result.endPoint.latLngDecimal.latitude, 0.0)
        assertEquals(175.456, result.endPoint.latLngDecimal.longitude, 0.0)

        assertEquals(90, result.heading.value)

        assertEquals(10.0, result.speed.value, 0.0)

        assertEquals(500.0, result.distance.value, 0.0)

        assertEquals(30, result.time)
    }
}