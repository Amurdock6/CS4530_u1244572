package com.example.assignment4.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDao {
    @Query("SELECT * FROM funfacts ORDER BY id DESC")
    fun getAll(): Flow<List<FunFact>>

    @Insert
    suspend fun insert(fact: FunFact)
}
