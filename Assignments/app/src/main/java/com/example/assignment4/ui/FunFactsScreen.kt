package com.example.assignment4.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.assignment4.data.local.FunFact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunFactsScreen(
    viewModel: FunFactsViewModel,
    containerColor: androidx.compose.ui.graphics.Color,
) {
    val facts = viewModel.facts.collectAsState()
    val loading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(error.value) {
        error.value?.let { snack.showSnackbar(it) }
    }

    LaunchedEffect(Unit) {
        if (facts.value.isEmpty()) viewModel.fetchNew()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fun Facts") }) },
        snackbarHost = { SnackbarHost(snack) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)                 // ← important
                .fillMaxSize()
                .padding(16.dp)                 // nice spacing
        ) {
            Button(
                onClick = { viewModel.fetchNew() },
                enabled = !loading.value,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(if (loading.value) "Fetching..." else "Fetch New Fun Fact")
            }

            if (facts.value.isEmpty()) {
                Text(
                    text = "No facts yet. Tap the button!",
                    modifier = Modifier.padding(top = 16.dp), // ← no fillMaxSize()
                    textAlign = TextAlign.Start
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(facts.value) { fact ->
                        FactCard(fact)
                    }
                }
            }
        }
    }
}

@Composable
private fun FactCard(fact: FunFact) {
    Card {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = fact.text)
            if (!fact.source.isNullOrBlank()) {
                Text(text = "Source: ${fact.source}")
            }
        }
    }
}
