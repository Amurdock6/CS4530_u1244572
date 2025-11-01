package com.example.assignment4.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Matches https://uselessfacts.jsph.pl/random.json?language=en
@Serializable
data class FactApiDto(
    val id: String? = null,
    val text: String,
    val source: String? = null,
    @SerialName("source_url") val sourceUrl: String? = null,
    val language: String? = null,
    val permalink: String? = null
)