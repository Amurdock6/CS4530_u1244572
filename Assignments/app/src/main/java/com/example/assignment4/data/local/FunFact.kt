package com.example.assignment4.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "funfacts")
data class FunFact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // Local PK
    val text: String,
    val source: String?,
    val sourceUrl: String?,
    val language: String?,
    val permalink: String?,
    val fetchedAt: Long // epoch millis
)
