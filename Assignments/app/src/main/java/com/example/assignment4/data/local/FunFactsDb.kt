package com.example.assignment4.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FunFact::class],
    version = 1,
    exportSchema = false
)
abstract class FunFactsDb : RoomDatabase() {
    abstract fun funFactDao(): FunFactDao
}
