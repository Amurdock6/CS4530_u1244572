package com.example.assignment2courseviewereditorapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assignment2courseviewereditorapp.model.Course

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val dept: String,
    val number: String,
    val location: String
)

// Mapping helpers
fun CourseEntity.toDomain() = Course(id, dept, number, location)
fun Course.toEntity() = CourseEntity(id, dept, number, location)
