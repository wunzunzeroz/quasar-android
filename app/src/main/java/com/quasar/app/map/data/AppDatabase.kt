package com.quasar.app.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.Waypoint

@Database(entities = [Waypoint::class, Polyline::class, Polygon::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waypointDao(): WaypointDao
    abstract fun polylineDao(): PolylineDao
    abstract fun polygonDao(): PolygonDao
}
