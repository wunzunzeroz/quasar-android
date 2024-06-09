package com.quasar.app.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quasar.app.map.models.Waypoint

class QuasarDatabase {
}
@Database(entities = [Waypoint::class], version = 1, exportSchema = false)
//@ypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waypointDao(): WaypointDao
}
