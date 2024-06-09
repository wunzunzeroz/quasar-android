package com.quasar.app.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quasar.app.map.models.Position
import com.quasar.app.map.models.Waypoint
import kotlinx.coroutines.flow.Flow

@Dao
interface WaypointDao {
    @Query("SELECT * FROM waypoint")
    fun getAll(): Flow<List<Waypoint>>

    @Query("SELECT * FROM waypoint WHERE id = :id")
    fun getById(id: Int): Flow<Waypoint>

    @Query("SELECT * FROM waypoint WHERE position = :pos")
    suspend fun getByPosition(pos: Position): Flow<Waypoint>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(waypoint: Waypoint)

    @Update
    suspend fun update(waypoint: Waypoint)

    @Delete()
    suspend fun delete(waypoint: Waypoint)

}
