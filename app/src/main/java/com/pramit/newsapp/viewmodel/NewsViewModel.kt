package com.pramit.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.repository.NewsRepository
import com.pramit.newsapp.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel  @Inject constructor(
    private val newsDao: NewsDao,
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val articles = repository.getNews()
                _uiState.value = UiState.Success(articles)
            } catch (e: Exception) {
                val cachedData = repository.getCachedNews()
                _uiState.value = if (cachedData.isNotEmpty()) {
                    UiState.CachedData(cachedData, "Showing cached data due to error: ${e.localizedMessage}")
                } else{
                    UiState.Error(e.localizedMessage)
                }
            }
        }
    }
}