package com.example.assignment4

import android.app.Application
import androidx.room.Room
import com.example.assignment4.data.FunFactsRepository
import com.example.assignment4.data.local.FunFactsDb
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FunFactsApp : Application() {
    // Room singleton
    val database: FunFactsDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            FunFactsDb::class.java,
            "funfacts.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // Ktor singleton
    val httpClient: HttpClient by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true   // API sends fields we don't map
                        isLenient = true
                    }
                )
            }
        }
    }

    // Repository singleton
    val repository: FunFactsRepository by lazy {
        FunFactsRepository(database.funFactDao(), httpClient)
    }
}
