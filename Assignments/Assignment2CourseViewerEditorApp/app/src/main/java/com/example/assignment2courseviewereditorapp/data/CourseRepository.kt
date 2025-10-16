package com.example.assignment2courseviewereditorapp.data

import com.example.assignment2courseviewereditorapp.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    val courses: Flow<List<Course>>
    suspend fun add(dept: String, number: String, location: String)
    suspend fun update(id: String, dept: String, number: String, location: String)
    suspend fun delete(id: String)
    suspend fun get(id: String): Course?
}
