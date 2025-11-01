package com.example.assignment4.data

import com.example.assignment4.data.api.FactApiDto
import com.example.assignment4.data.local.FunFact
import com.example.assignment4.data.local.FunFactDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FunFactsRepository(
    private val dao: FunFactDao,
    private val client: HttpClient
) {
    // Stream of all saved facts
    fun observeFacts(): Flow<List<FunFact>> = dao.getAll()

    // Fetch from API and persist
    suspend fun fetchAndSaveRandom(): FunFact = withContext(Dispatchers.IO) {
        val dto: FactApiDto = client
            .get("https://uselessfacts.jsph.pl/random.json?language=en")
            .body()

        val entity = FunFact(
            text = dto.text,
            source = dto.source,
            sourceUrl = dto.sourceUrl,
            language = dto.language,
            permalink = dto.permalink,
            fetchedAt = System.currentTimeMillis()
        )
        dao.insert(entity)
        entity
    }
}
