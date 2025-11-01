package com.example.assignment4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.assignment4.ui.FunFactsScreen
import com.example.assignment4.ui.FunFactsViewModel
import com.example.assignment4.ui.FunFactsViewModelFactory
import com.example.assignment4.ui.theme.Assignment4Theme

class MainActivity : ComponentActivity() {

    private val viewModel: FunFactsViewModel by viewModels {
        val app = application as FunFactsApp
        FunFactsViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment4Theme {
                FunFactsScreen(
                    viewModel = viewModel,
                    containerColor = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}
