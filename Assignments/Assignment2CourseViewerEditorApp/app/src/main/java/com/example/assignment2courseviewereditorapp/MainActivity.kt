package com.example.assignment2courseviewereditorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.assignment2courseviewereditorapp.model.Course
import com.example.assignment2courseviewereditorapp.ui.AddOrEditCourseScreen
import com.example.assignment2courseviewereditorapp.ui.CourseDetailScreen
import com.example.assignment2courseviewereditorapp.ui.CourseListScreen
import com.example.assignment2courseviewereditorapp.viewmodel.CourseViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * The single Activity hosting our Compose UI.
 *
 * Uses [viewModels] to obtain a [CourseViewModel] scoped to the Activity.
 * Content is set via [setContent], rendering the [CourseApp] composable tree.
 */
class MainActivity : ComponentActivity() {
    private val vm: CourseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseApp(vm)
        }
    }
}

/**
 * Root composable coordinating navigation between screens and reading VM state.
 *
 * It collects [CourseViewModel.courses] and [CourseViewModel.selectedCourseId],
 * then switches UI using a simple [ScreenMode] enum. No Navigation component is
 * required for this assignment, keeping the app single‑activity and simple.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseApp(vm: CourseViewModel) {
    val courses by vm.courses.collectAsState()
    val selectedId by vm.selectedCourseId.collectAsState()

    // Simple UI mode machine (List, Detail, Add, Edit)
    var mode by remember { mutableStateOf(ScreenMode.List) }
    var editingCourse: Course? by remember { mutableStateOf(null) }


    val selectedCourse = courses.firstOrNull { it.id == selectedId }


    // Basic light theme using Material 3.
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            when (mode) {
                ScreenMode.List -> CourseListScreen(
                    courses = courses,
                    onCourseClick = { id -> vm.selectCourse(id); mode = ScreenMode.Detail },
                    onAddClick = { editingCourse = null; mode = ScreenMode.Add },
                    onDeleteClick = { id -> vm.deleteCourse(id) }
                )
                ScreenMode.Detail -> {
                    if (selectedCourse != null) {
                        CourseDetailScreen(
                            course = selectedCourse,
                            onBack = { vm.clearSelection(); mode = ScreenMode.List },
                            onDelete = { id -> vm.deleteCourse(id); mode = ScreenMode.List },
                            onEdit = { c -> editingCourse = c; mode = ScreenMode.Edit }
                        )
                    } else {
                        // If nothing selected, fall back to list
                        mode = ScreenMode.List
                    }
                }
                ScreenMode.Add -> AddOrEditCourseScreen(
                    title = "Add Course",
                    initial = null,
                    onCancel = { mode = ScreenMode.List },
                    onSave = { dept, number, location ->
                        vm.addCourse(dept, number, location)
                        mode = ScreenMode.List
                    }
                )
                ScreenMode.Edit -> AddOrEditCourseScreen(
                    title = "Edit Course",
                    initial = editingCourse,
                    onCancel = { mode = ScreenMode.Detail },
                    onSave = { dept, number, location ->
                        editingCourse?.let {
                            vm.updateCourse(it.id, dept, number, location)
                            vm.selectCourse(it.id) // keep detail focused on the edited course
                        }
                        mode = ScreenMode.Detail
                    }
                )
            }
        }
    }
}

/** UI mode states for the simple in‑app navigator. */
enum class ScreenMode { List, Detail, Add, Edit }