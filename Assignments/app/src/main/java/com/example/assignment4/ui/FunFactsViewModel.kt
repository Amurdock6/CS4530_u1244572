package com.example.assignment4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment4.data.FunFactsRepository
import com.example.assignment4.data.local.FunFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FunFactsViewModel(private val repo: FunFactsRepository) : ViewModel() {

    private val _facts = MutableStateFlow<List<FunFact>>(emptyList())
    val facts: StateFlow<List<FunFact>> = _facts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Start collecting DB immediately
        viewModelScope.launch {
            repo.observeFacts()
                .catch { _error.value = it.message }
                .collectLatest { _facts.value = it }
        }
    }

    fun fetchNew() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repo.fetchAndSaveRandom()
            } catch (t: Throwable) {
                _error.value = t.message ?: "Failed to fetch fact."
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class FunFactsViewModelFactory(
    private val repo: FunFactsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FunFactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FunFactsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
