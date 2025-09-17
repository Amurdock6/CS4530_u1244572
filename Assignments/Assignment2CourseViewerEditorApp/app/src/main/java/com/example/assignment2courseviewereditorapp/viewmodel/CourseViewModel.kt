package com.example.assignment2courseviewereditorapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.assignment2courseviewereditorapp.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for holding and mutating UI state for the Course app.
 *
 * Exposes a read‑only [courses] flow to the UI and provides mutation methods to
 * add, update, delete courses as well as track a [selectedCourseId] for detail view.
 *
 * Architecture:
 * - VM owns state (Single source of truth)
 * - Composable collect StateFlows and render UI accordingly
 */
class CourseViewModel : ViewModel() {

    // Backing state for list of courses; seeded with a few examples for demo.
    private val _courses = MutableStateFlow(
        listOf(
            Course(dept = "CS", number = "4530", location = "MEB 3147"),
            Course(dept = "CS", number = "3500", location = "WEB L104"),
            Course(dept = "CS", number = "2420", location = "WEB 1230")
        )
    )

    /** Public, read‑only stream of courses for the UI to observe. */
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    // Holds the currently selected course ID (for the detail screen); nullable when none selected.
    private val _selectedCourseId = MutableStateFlow<String?>(null)

    /** Public, read‑only stream of the selected course ID. */
    val selectedCourseId: StateFlow<String?> = _selectedCourseId.asStateFlow()

    /** Select a course to view its details. */
    fun selectCourse(id: String) { _selectedCourseId.value = id }
    /** Clear the current selection (e.g., when navigating back). */
    fun clearSelection() { _selectedCourseId.value = null }

    /**
     * Add a new [Course] to the list.
     * @param dept Department code (e.g., "CS")
     * @param number Course number (e.g., "4530")
     * @param location Room/building string
     */
    fun addCourse(dept: String, number: String, location: String) {
        val newCourse = Course(dept = dept, number = number, location = location)
        _courses.value = _courses.value + newCourse
    }

    /**
     * Delete a course by ID.
     * If the deleted course was selected, clear the selection.
     */
    fun deleteCourse(id: String) {
        _courses.value = _courses.value.filterNot { it.id == id }
        if (_selectedCourseId.value == id) {
            _selectedCourseId.value = null
        }
    }

    /**
     * Update an existing course by ID.
     * Uses [java.util.Collections.copy] to preserve immutability and trigger recomposition.
     */
    fun updateCourse(id: String, dept: String, number: String, location: String) {
        _courses.value = _courses.value.map { c ->
            if (c.id == id) c.copy(dept = dept, number = number, location = location) else c
        }
    }
}