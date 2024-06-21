package com.quasar.app.map.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.quasar.app.R
import com.quasar.app.map.models.Waypoint
import com.quasar.app.map.models.WaypointMarkerType

@OptIn(MapboxExperimental::class)
@Composable
fun MapWaypoints(
    waypoints: List<Waypoint>, onWaypointClicked: (Waypoint) -> Unit
) {
    val waypointAnnotations = waypoints.map {
        PointAnnotationOptions().withPoint(it.position.toPoint())
            .withIconImage(getMarkerBitmap(LocalContext.current, it.markerType))
    }

    val circleAnnotations = waypoints.map {
        CircleAnnotationOptions().withPoint(it.position.toPoint())
            .withCircleColor(it.color).withCircleRadius(10.0).withCircleOpacity(0.7)
    }

    CircleAnnotationGroup(annotations = circleAnnotations, onClick = {
        val wpt = waypoints.first { w -> w.position.toPoint() == it.point }

        onWaypointClicked(wpt)

        true
    })

    PointAnnotationGroup(annotations = waypointAnnotations, onClick = {
        val wpt = waypoints.first { w -> w.position.toPoint() == it.point }

        onWaypointClicked(wpt)

        true
    })
}
private fun getMarkerBitmap(context: Context, markerType: WaypointMarkerType): Bitmap {
    val drawableId = getDrawableForWaypointMarker(markerType)
    val drawable = ContextCompat.getDrawable(context, drawableId)

    if (drawable is BitmapDrawable) {
        return resizeBitmap(drawable.bitmap, 30)
    }
    throw Exception("Unable to get bitmap for marker type: $markerType")
}

private fun resizeBitmap(bitmap: Bitmap, size: Int = 40): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, size, size, false)
}

fun getDrawableForWaypointMarker(markerType: WaypointMarkerType): Int {
    return when (markerType) {
        WaypointMarkerType.Flag -> R.drawable.flag
        WaypointMarkerType.Marker -> R.drawable.marker
        WaypointMarkerType.Pin -> R.drawable.marker
        WaypointMarkerType.Cross -> R.drawable.cross
        WaypointMarkerType.Circle -> R.drawable.check_mark
        WaypointMarkerType.Triangle -> R.drawable.triangle
        WaypointMarkerType.Square -> R.drawable.marker
        WaypointMarkerType.Star -> R.drawable.star
        WaypointMarkerType.QuestionMark -> R.drawable.question_mark
        WaypointMarkerType.ExclamationPoint -> R.drawable.exclamation_mark
        WaypointMarkerType.CheckMark -> R.drawable.check_mark
        WaypointMarkerType.CrossMark -> R.drawable.cross_mark
        WaypointMarkerType.Car -> R.drawable.car
        WaypointMarkerType.Boat -> R.drawable.boat
        WaypointMarkerType.Plane -> R.drawable.plane
        WaypointMarkerType.Helicopter -> R.drawable.helicopter
        WaypointMarkerType.Forest -> R.drawable.forest
        WaypointMarkerType.Water -> R.drawable.water
        WaypointMarkerType.Mountain -> R.drawable.mountains
        WaypointMarkerType.Beach -> R.drawable.beach
        WaypointMarkerType.Fire -> R.drawable.fire
        WaypointMarkerType.Anchor -> R.drawable.anchor
        WaypointMarkerType.Lifering -> R.drawable.lifering
        WaypointMarkerType.Target -> R.drawable.target
        WaypointMarkerType.Tent -> R.drawable.tent
        WaypointMarkerType.House -> R.drawable.house
        WaypointMarkerType.Building -> R.drawable.building
        WaypointMarkerType.Castle -> R.drawable.castle
        WaypointMarkerType.Footprints -> R.drawable.footsteps
        WaypointMarkerType.Person -> R.drawable.person
        WaypointMarkerType.People -> R.drawable.people
        WaypointMarkerType.Skull -> R.drawable.skull
        WaypointMarkerType.Drinks -> R.drawable.drinks
        WaypointMarkerType.Food -> R.drawable.food
        WaypointMarkerType.WaterSource -> R.drawable.water_source
        WaypointMarkerType.Fuel -> R.drawable.fuel
    }
}
