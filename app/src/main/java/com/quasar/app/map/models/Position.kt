package com.quasar.app.map.models

import com.mapbox.geojson.Point
import com.quasar.app.map.utils.Utils
import org.locationtech.proj4j.BasicCoordinateTransform
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateReferenceSystem
import org.locationtech.proj4j.ProjCoordinate
import kotlin.math.absoluteValue

class Position(latitude: Double, longitude: Double) {
    val latLngDecimal: LatLngDecimal = LatLngDecimal(latitude, longitude)
    val gridReference: GridReference = convertLatLngToGridRef(latLngDecimal)
    val latLngDegreesMinutes: LatLngDegreesMinutes = convertDecimalToDegreesMinutes(latLngDecimal)

    fun toPoint(): Point {
        return Point.fromLngLat(latLngDecimal.longitude, latLngDecimal.latitude)
    }

    fun toShareableString(): String {
        return """
        LOCATION:
        
        Grid reference:
        E${gridReference.eastings}
        N${gridReference.northings}
        
        LAT/LNG DD:
        ${latLngDecimal.latitude}, ${latLngDecimal.longitude}
        
        LAT/LNG DDM:
        ${latLngDegreesMinutes.latitude}
        ${latLngDegreesMinutes.longitude}
        
        GOOGLE MAPS LINK:
        https://google.com/maps?q=${latLngDecimal.latitude},${latLngDecimal.longitude}
    """.trimIndent()
    }

    companion object {
        fun fromPoint(point: Point): Position {
            return Position(point.latitude(), point.latitude())
        }

        fun fromGridRef(input: GridReference): Position {
            val latLng = convertGridRefToLatLng(input)
            println("GR->LL: Lat: ${latLng.latitude}, Lng: ${latLng.longitude}")

            return Position(latLng.latitude, latLng.longitude)
        }

        fun fromDegreesMinutes(
            latDeg: Int,
            latMin: Double,
            latDir: CardinalDirection,
            lngDeg: Int,
            lngMin: Double,
            lngDir: CardinalDirection
        ): Position {
            val latitude = convertDegreesMinutesToDecimal(latDeg, latMin, latDir)
            val longitude = convertDegreesMinutesToDecimal(lngDeg, lngMin, lngDir)

            return Position(latitude, longitude)
        }

        private fun convertDegreesMinutesToDecimal(
            degrees: Int, minutes: Double, cardinalDirection: CardinalDirection
        ): Double {
            val result = degrees + (minutes / 60.0)

            return if (cardinalDirection == CardinalDirection.S || cardinalDirection == CardinalDirection.W) {
                result * -1
            } else {
                result
            }
        }

        private fun convertDecimalToDegreesMinutes(input: LatLngDecimal): LatLngDegreesMinutes {
            val latitude = convertToDegreesMinutes(input.latitude, true)
            val longitude = convertToDegreesMinutes(input.longitude, false)

            return LatLngDegreesMinutes(latitude, longitude)
        }

        private fun convertToDegreesMinutes(input: Double, isLatitude: Boolean): DegreesMinutes {
            val decimal = input.absoluteValue % 1
            val degrees = (input.absoluteValue - decimal).toInt()
            val minutes = Utils.RoundNumberToDp(decimal * 60, 4)

            val direction = if (isLatitude) {
                if (input >= 0) CardinalDirection.N else CardinalDirection.S
            } else {
                if (input >= 0) CardinalDirection.E else CardinalDirection.W
            }

            return DegreesMinutes(degrees, minutes, direction)
        }

        private fun convertGridRefToLatLng(input: GridReference): LatLngDecimal {
            val crsFactory = CRSFactory()

            // Define WGS84 and NZTM coordinate reference systems
            val wgs84Crs: CoordinateReferenceSystem = crsFactory.createFromName("EPSG:4326")
            val nztmCrs: CoordinateReferenceSystem = crsFactory.createFromName("EPSG:2193")

            // Create a coordinate transformer
            val ct = BasicCoordinateTransform(nztmCrs, wgs84Crs)

            // Create a coordinate object for the source coordinates
            val nztmCoord = ProjCoordinate(input.eastings.toDouble(), input.northings.toDouble())

            // Create a coordinate object to hold the result
            val wgs84Coord = ProjCoordinate()

            // Perform the transformation
            ct.transform(nztmCoord, wgs84Coord)

            return LatLngDecimal(wgs84Coord.y, wgs84Coord.x)
        }

        private fun convertLatLngToGridRef(input: LatLngDecimal): GridReference {
            val crsFactory = CRSFactory()

            // Define WGS84 and NZTM coordinate reference systems
            val wgs84Crs: CoordinateReferenceSystem = crsFactory.createFromName("EPSG:4326")
            val nztmCrs: CoordinateReferenceSystem = crsFactory.createFromName("EPSG:2193")

            // Create a coordinate transformer
            val ct = BasicCoordinateTransform(wgs84Crs, nztmCrs)

            // Create a coordinate object for the source coordinates
            val wgs84Coord = ProjCoordinate(input.longitude, input.latitude)

            // Create a coordinate object to hold the result
            val nztmCoord = ProjCoordinate()

            // Perform the transformation
            ct.transform(wgs84Coord, nztmCoord)

            return GridReference(nztmCoord.x.toInt(), nztmCoord.y.toInt())
        }

    }
}

data class GridReference(val eastings: Int, val northings: Int) {
    override fun toString(): String {
        return "E$eastings, N$northings"
    }
}

data class LatLngDecimal(var latitude: Double, var longitude: Double) {
    init {
        latitude = Utils.RoundNumberToDp(latitude, 6)
        longitude = Utils.RoundNumberToDp(longitude, 6)
    }

    override fun toString(): String {
        return "$latitude, $longitude"
    }
}

data class LatLngDegreesMinutes(val latitude: DegreesMinutes, val longitude: DegreesMinutes) {
    override fun toString(): String {
        return "$latitude, $longitude"
    }
}

data class DegreesMinutes(
    val degrees: Int, val minutes: Double, val cardinalDirection: CardinalDirection
) {
    override fun toString(): String {
        return "$degrees° $minutes' $cardinalDirection"
    }
}

enum class CardinalDirection {
    N, S, E, W;

    companion object {
        fun parse(str: String): CardinalDirection {
            val result = entries.firstOrNull { it.name == str }

            return result
                ?: throw IllegalStateException("Unable to parse string in CardinalDirection: $str")
        }
    }
}
