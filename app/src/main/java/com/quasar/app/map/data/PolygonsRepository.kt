package com.quasar.app.map.data

import com.quasar.app.map.models.Polygon
import kotlinx.coroutines.flow.Flow

interface PolygonsRepository {
    fun getAll(): Flow<List<Polygon>>
    suspend fun insert(polygon: Polygon)
    suspend fun delete(polygon: Polygon)
    suspend fun update(polygon: Polygon)
}

class PolygonsRepositoryImpl(private val dao: PolygonDao) : PolygonsRepository {
    override fun getAll(): Flow<List<Polygon>> = dao.getAll()

    override suspend fun insert(polygon: Polygon) = dao.insert(polygon)

    override suspend fun delete(polygon: Polygon) = dao.delete(polygon)

    override suspend fun update(polygon: Polygon) = dao.update(polygon)
}