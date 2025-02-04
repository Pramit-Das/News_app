package com.pramit.newsapp.model

data class NewsUIState(
    val isLoading: Boolean = false,
    val newsArticles: List<NewsArticle> = emptyList(),
    val errorMessage: String? = null
)
