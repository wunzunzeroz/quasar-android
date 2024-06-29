package com.quasar.app.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quasar.app.map.models.Polyline
import com.quasar.app.map.models.SearchPattern
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchPatternDao {
    @Query("SELECT * FROM search_pattern")
    fun getAll(): Flow<List<SearchPattern>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(searchPattern: SearchPattern)

    @Update
    suspend fun update(searchPattern: SearchPattern)

    @Delete()
    suspend fun delete(searchPattern: SearchPattern)
}