package com.pramit.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pramit.newsapp.model.NewsUIState
import com.pramit.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUIState())
    val uiState: StateFlow<NewsUIState> = _uiState

    init {
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try {
                val articles = repository.getNews()
                _uiState.value = NewsUIState(newsArticles = articles)
            } catch (e: Exception) {
                _uiState.value = NewsUIState(errorMessage = "Network error, showing cached data.")
                _uiState.value = _uiState.value.copy(newsArticles = repository.getCachedNews())
            }
        }
    }
}
