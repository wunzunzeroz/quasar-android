package com.quasar.app.map.data

import com.quasar.app.map.models.SearchPattern
import kotlinx.coroutines.flow.Flow

interface SearchPatternsRepository {
    fun getAll(): Flow<List<SearchPattern>>
    suspend fun insert(searchPattern: SearchPattern)
    suspend fun delete(searchPattern: SearchPattern)
    suspend fun update(searchPattern: SearchPattern)
}

class SearchPatternsRepositoryImpl(private val dao: SearchPatternDao) : SearchPatternsRepository {
    override fun getAll(): Flow<List<SearchPattern>> = dao.getAll()

    override suspend fun insert(searchPattern: SearchPattern) = dao.insert(searchPattern)

    override suspend fun delete(searchPattern: SearchPattern) = dao.delete(searchPattern)

    override suspend fun update(searchPattern: SearchPattern) = dao.update(searchPattern)
}