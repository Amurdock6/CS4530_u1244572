package com.example.assignment2courseviewereditorapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment2courseviewereditorapp.data.CourseRepository
import com.example.assignment2courseviewereditorapp.model.Course
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
class CourseViewModel(
    private val repo: CourseRepository
) : ViewModel() {

    // Live list from Room. Persists across app restarts.
    val courses: StateFlow<List<Course>> =
        repo.courses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Holds the currently selected course ID (for the detail screen); nullable when none selected. */
    private val _selectedCourseId = MutableStateFlow<String?>(null)

    /** Public, read‑only stream of the selected course ID. */
    val selectedCourseId: StateFlow<String?> = _selectedCourseId.asStateFlow()


    /** Select a course to view its details. */
    fun selectCourse(id: String) {
        _selectedCourseId.value = id
    }

    /** Clear the current selection (e.g., when navigating back). */
    fun clearSelection() {
        _selectedCourseId.value = null
    }

    /**
     * Add a new [Course] to the list.
     * @param dept Department code (e.g., "CS")
     * @param number Course number (e.g., "4530")
     * @param location Room/building string
     */
    fun addCourse(dept: String, number: String, location: String) =
        viewModelScope.launch { repo.add(dept, number, location) }

    /**
     * Delete a course by ID.
     * If the deleted course was selected, clear the selection.
     */
    fun deleteCourse(id: String) = viewModelScope.launch {
        repo.delete(id)
        if (selectedCourseId.value == id) clearSelection()
    }

    /**
     * Update an existing course by ID.
     * Uses [java.util.Collections.copy] to preserve immutability and trigger recomposition.
     */
    fun updateCourse(id: String, dept: String, number: String, location: String) =
        viewModelScope.launch { repo.update(id, dept, number, location) }
}

    class CourseViewModelFactory(
        private val repo: CourseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CourseViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }