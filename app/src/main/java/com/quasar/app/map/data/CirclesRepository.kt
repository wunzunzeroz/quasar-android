package com.quasar.app.map.data

import com.quasar.app.map.models.Circle
import kotlinx.coroutines.flow.Flow

interface CirclesRepository {
    fun getAll(): Flow<List<Circle>>
    suspend fun insert(circle: Circle)
    suspend fun delete(circle: Circle)
    suspend fun update(circle: Circle)
}

class CirclesRepositoryImpl(private val dao: CircleDao) : CirclesRepository {
    override fun getAll(): Flow<List<Circle>> = dao.getAll()

    override suspend fun insert(circle: Circle) = dao.insert(circle)

    override suspend fun delete(circle: Circle) = dao.delete(circle)

    override suspend fun update(circle: Circle) = dao.update(circle)
}