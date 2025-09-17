package com.example.assignment2courseviewereditorapp.model

import java.util.UUID

/**
 * Immutable domain model representing a course in the app.
 *
 * Each course has a unique [id] used as a stable key in lists, a department code
 * (e.g., "CS"), a course [number] (e.g., "4530"), and a [location] (e.g., "MEB 3147").
 *
 * The [shortName] computed property normalizes the department and combines it with the
 * number for compact display in lists.
 */
data class Course(
    val id: String = UUID.randomUUID().toString(),
    val dept: String,
    val number: String,
    val location: String
) {
    /** A compact name like "CS 4530" for list rows. */
    val shortName: String get() = "${dept.trim().uppercase()} ${number.trim()}"
}