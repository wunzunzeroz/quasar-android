package com.quasar.app.map.data

import com.quasar.app.map.models.Waypoint
import kotlinx.coroutines.flow.Flow

interface WaypointsRepository {
    fun getAllWaypoints(): Flow<List<Waypoint>>
    fun getWaypointById(id: Int): Flow<Waypoint?>
    suspend fun insertWaypoint(waypoint: Waypoint)
    suspend fun deleteWaypoint(waypoint: Waypoint)
    suspend fun updateWaypoint(waypoint: Waypoint)
}

class WaypointsRepositoryImpl(private val waypointDao: WaypointDao) : WaypointsRepository {
    override fun getAllWaypoints(): Flow<List<Waypoint>> = waypointDao.getAll()

    override fun getWaypointById(id: Int): Flow<Waypoint?> = waypointDao.getById(id)

    override suspend fun insertWaypoint(waypoint: Waypoint) = waypointDao.insert(waypoint)

    override suspend fun deleteWaypoint(waypoint: Waypoint) = waypointDao.delete(waypoint)

    override suspend fun updateWaypoint(waypoint: Waypoint) = waypointDao.update(waypoint)

}