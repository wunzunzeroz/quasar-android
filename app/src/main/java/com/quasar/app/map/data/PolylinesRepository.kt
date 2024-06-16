package com.quasar.app.map.data

import com.quasar.app.map.models.Polyline
import kotlinx.coroutines.flow.Flow

interface PolylinesRepository {
    fun getAll(): Flow<List<Polyline>>
    suspend fun insert(polyline: Polyline)
    suspend fun delete(polyline: Polyline)
    suspend fun update(polyline: Polyline)
}

class PolylinesRepositoryImpl(private val dao: PolylineDao) : PolylinesRepository {
    override fun getAll(): Flow<List<Polyline>> = dao.getAll()

    override suspend fun insert(polyline: Polyline) = dao.insert(polyline)

    override suspend fun delete(polyline: Polyline) = dao.delete(polyline)

    override suspend fun update(polyline: Polyline) = dao.update(polyline)
}