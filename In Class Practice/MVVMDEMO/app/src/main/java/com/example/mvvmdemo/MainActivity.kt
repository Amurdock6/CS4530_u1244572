package com.example.mvvmdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

// ⬇️ If your builder’s theme function has a different name, update this import.
// For example:
// import com.example.mvvmdemo.theme.MaterialThemeBuilderTheme as AppTheme
// or: import com.example.mvvmdemo.ui.theme.MyApplicationTheme as AppTheme
// If you don’t have a wrapper yet, I can show you how to make one from the md_theme_* tokens.

class MyViewModel : ViewModel() {
    private val taskMutable = kotlinx.coroutines.flow.MutableStateFlow(listOf<String>())
    val tasksReadOnly : kotlinx.coroutines.flow.StateFlow<List<String>> = taskMutable

    fun addItem(item: String) {
        if (item.isNotBlank()) taskMutable.value = taskMutable.value + item
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // IMPORTANT:
            // Set useDynamicColor = false if you want to strictly use the palette
            // you made in Material Theme Builder (recommended while you’re verifying colors).
            AppThemeWrapper(useDynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm: MyViewModel = viewModel()
                    TodoList(vm)
                }
            }
        }
    }
}

/**
 * Thin wrapper so you can point at whatever the builder exported.
 * Replace the body with your generated theme function call.
 */
@Composable
private fun AppThemeWrapper(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // If your builder exported `MaterialThemeBuilderTheme(darkTheme, dynamicColor) { ... }`
    // just call that here. Example:
    //
    // MaterialThemeBuilderTheme(
    //     darkTheme = darkTheme,
    //     dynamicColor = useDynamicColor,
    //     content = content
    // )
    //
    // If it exported `MyApplicationTheme`, call that instead.

    // TEMP fallback: if you haven’t wired the generated theme yet,
    // you can keep using your old ComposeDEMOTheme here so the app still compiles:
    com.example.mvvmdemo.ui.theme.ComposeDEMOTheme(
        darkTheme = darkTheme,
        dynamicColor = useDynamicColor,
        content = content
    )
}

@Composable
fun TodoList(myVM: MyViewModel) {
    val observableList by myVM.tasksReadOnly.collectAsState()
    var itemText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // Text field + button
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = itemText,
                onValueChange = { newText -> itemText = newText },
                label = { Text("Item") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                myVM.addItem(itemText)
                itemText = ""
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(20.dp))

        // Use theme typography & colors (no hard-coded Color.Blue)
        Text(
            "ToDo List",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(observableList) { item ->
                Text(
                    item,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
