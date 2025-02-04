package com.pramit.newsapp.repository

import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.model.NewsArticle
import com.pramit.newsapp.network.NewsApi
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val api: NewsApi
) {
    suspend fun getNews(): List<NewsArticle> {
        val response = api.getTopHeadlines("us", "YOUR_API_KEY")
        newsDao.insertAll(response.articles)
        return response.articles
    }

    suspend fun getCachedNews(): List<NewsArticle> = newsDao.getAllArticles().firstOrNull() ?: emptyList()
}