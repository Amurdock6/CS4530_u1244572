package com.example.assignment2courseviewereditorapp.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.assignment2courseviewereditorapp.model.Course

/**
 * List screen displaying all courses. Shows only the compact course name (e.g., "CS 4530").
 *
 * Tapping a row invokes [onCourseClick] with the course ID. The FAB starts the add flow.
 * Each row also offers a delete icon that calls [onDeleteClick].
 *
 * @param courses Current course list to display
 * @param onCourseClick Callback when a row is clicked (receives course ID)
 * @param onAddClick Callback when the floating action button is pressed
 * @param onDeleteClick Callback when delete is pressed (receives course ID)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    courses: List<Course>,
    onCourseClick: (String) -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Courses") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }
    ) { padding ->
        if (courses.isEmpty()) {
            // Empty state message keeps UI friendly when no data exists.
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No courses yet. Tap + to add one.")
            }
        } else {
            // LazyColumn efficiently renders potentially long lists.
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                items(courses, key = { it.id }) { course ->
                    ListItem(
                        headlineContent = { Text(course.shortName) },
                        supportingContent = { Text(course.location) },
                        trailingContent = {
                            IconButton(onClick = { onDeleteClick(course.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete ${course.shortName}")
                            }
                        },
                        modifier = Modifier.clickable { onCourseClick(course.id) }
                    )
                    // Material3 divider between rows.
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
        }
    }
}


/**
 * Detail screen for a single [Course].
 *
 * Provides Back, Edit, and Delete actions in the TopAppBar. Editing is optional extra credit
 * and is surfaced via the [onEdit] callback to the parent.
 *
 * @param course The course being displayed
 * @param onBack Invoked when back arrow is pressed
 * @param onDelete Invoked when delete icon is pressed (receives course ID)
 * @param onEdit Invoked when edit icon is pressed (receives the full course)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    course: Course,
    onBack: () -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (Course) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course.shortName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(course) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDelete(course.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Department: ${course.dept}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Number: ${course.number}", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Location: ${course.location}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * Add/Edit screen used for both creating and updating a [Course].
 *
 * When [initial] is null, the screen behaves as an "Add" form.
 * Otherwise, it pre‑populates fields with the existing course values (Edit mode).
 * [onSave] only becomes enabled when all fields are non‑blank.
 *
 * @param title Top bar title (e.g., "Add Course" or "Edit Course")
 * @param initial The existing course to edit, or null to add a new one
 * @param onCancel Invoked when the user cancels
 * @param onSave Invoked with (dept, number, location) when Save is pressed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditCourseScreen(
    title: String,
    initial: Course? = null,
    onCancel: () -> Unit,
    onSave: (dept: String, number: String, location: String) -> Unit
) {
    // Remember transient form state for Compose recomposition.
    var dept by remember { mutableStateOf(initial?.dept ?: "") }
    var number by remember { mutableStateOf(initial?.number ?: "") }
    var location by remember { mutableStateOf(initial?.location ?: "") }

    // Simple validation to guard the Save button.
    val canSave = dept.isNotBlank() && number.isNotBlank() && location.isNotBlank()


    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = dept,
                onValueChange = { dept = it },
                label = { Text("Department (e.g., CS)") },
                singleLine = true
            )
            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Course Number (e.g., 4530)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location (e.g., WEB L104)") },
                singleLine = true
            )


            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
                Button(onClick = { onSave(dept, number, location) }, enabled = canSave) {
                    Text("Save")
                }
            }
        }
    }
}