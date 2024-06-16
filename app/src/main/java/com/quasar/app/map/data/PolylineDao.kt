package com.quasar.app.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quasar.app.map.models.Polyline
import kotlinx.coroutines.flow.Flow

@Dao
interface PolylineDao {
    @Query("SELECT * FROM polyline")
    fun getAll(): Flow<List<Polyline>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(polyline: Polyline)

    @Update
    suspend fun update(polyline: Polyline)

    @Delete()
    suspend fun delete(polyline: Polyline)
}