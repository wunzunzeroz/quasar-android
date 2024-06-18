package com.quasar.app.map.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Polygon
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import com.mapbox.turf.TurfTransformation
import com.quasar.app.map.components.DistanceUnit

@Entity
class Circle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val center: Position,
    val radius: Double,
    val distanceUnit: DistanceUnit
) {
    private fun turfUnit(): String {
        return when (distanceUnit) {
            DistanceUnit.Metres -> TurfConstants.UNIT_METRES
            DistanceUnit.Kilometres -> TurfConstants.UNIT_KILOMETERS
            DistanceUnit.NauticalMiles -> TurfConstants.UNIT_NAUTICAL_MILES
        }
    }

    private fun toPolygon(): Polygon {
        return TurfTransformation.circle(center.toPoint(), radius, turfUnit())
    }

    fun toGeoJsonString(): String {
        return FeatureCollection.fromFeature(Feature.fromGeometry(toPolygon())).toJson()
    }
}