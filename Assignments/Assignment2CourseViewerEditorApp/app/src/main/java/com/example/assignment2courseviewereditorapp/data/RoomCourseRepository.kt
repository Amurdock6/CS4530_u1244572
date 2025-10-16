package com.example.assignment2courseviewereditorapp.data

import com.example.assignment2courseviewereditorapp.data.local.CourseDao
import com.example.assignment2courseviewereditorapp.data.local.toDomain
import com.example.assignment2courseviewereditorapp.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import com.example.assignment2courseviewereditorapp.data.local.CourseEntity

class RoomCourseRepository(private val dao: CourseDao) : CourseRepository {
    override val courses: Flow<List<Course>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun add(dept: String, number: String, location: String) {
        val entity = CourseEntity(UUID.randomUUID().toString(), dept, number, location)
        dao.upsert(entity)
    }

    override suspend fun update(id: String, dept: String, number: String, location: String) {
        dao.upsert(CourseEntity(id, dept, number, location))
    }

    override suspend fun delete(id: String) = dao.deleteById(id)

    override suspend fun get(id: String): Course? = dao.getById(id)?.toDomain()
}
