package com.pramit.newsapp.ui.state

import com.pramit.newsapp.model.Article

sealed class UiState {
    object Loading : UiState()
    data class Success(val articles: List<Article>) : UiState()
    data class CachedData(val articles: List<Article>, val message: String) : UiState()
    data class Error(val message: String) : UiState()
}