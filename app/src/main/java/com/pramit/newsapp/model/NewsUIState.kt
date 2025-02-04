package com.pramit.newsapp.model

data class NewsUIState(
    val isLoading: Boolean = false,
    val newsArticles: List<Article> = emptyList(),
    val errorMessage: String? = null
)
