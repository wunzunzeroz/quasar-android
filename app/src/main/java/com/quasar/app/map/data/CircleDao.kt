package com.quasar.app.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quasar.app.map.models.Circle
import com.quasar.app.map.models.Polygon
import com.quasar.app.map.models.Polyline
import kotlinx.coroutines.flow.Flow

@Dao
interface CircleDao {
    @Query("SELECT * FROM circle")
    fun getAll(): Flow<List<Circle>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(circle: Circle)

    @Update
    suspend fun update(circle: Circle)

    @Delete()
    suspend fun delete(circle: Circle)
}