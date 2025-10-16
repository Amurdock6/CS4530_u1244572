package com.example.assignment2courseviewereditorapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile private var INSTANCE: CourseDatabase? = null

        fun get(context: Context): CourseDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "courses.db"
                ).build().also { INSTANCE = it }
            }
    }
}
