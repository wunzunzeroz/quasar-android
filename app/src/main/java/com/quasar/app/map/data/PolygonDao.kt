package com.quasar.app.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.Polyline
import kotlinx.coroutines.flow.Flow

@Dao
interface PolygonDao {
    @Query("SELECT * FROM polygon")
    fun getAll(): Flow<List<Polygon>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(polygon: Polygon)

    @Update
    suspend fun update(polygon: Polygon)

    @Delete()
    suspend fun delete(polygon: Polygon)
}