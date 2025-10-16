package com.example.assignment2courseviewereditorapp

import android.app.Application
import com.example.assignment2courseviewereditorapp.data.CourseRepository
import com.example.assignment2courseviewereditorapp.data.RoomCourseRepository
import com.example.assignment2courseviewereditorapp.data.local.CourseDatabase

class CourseApplication : Application() {
    // App-wide singletons kept here
    lateinit var repository: CourseRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = CourseDatabase.get(this)
        repository = RoomCourseRepository(db.courseDao())
    }
}
