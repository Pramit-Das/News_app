package com.pramit.newsapp.repository

import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.model.Article
import com.pramit.newsapp.network.NewsApi
import com.pramit.newsapp.util.ensureUniqueIds
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val api: NewsApi
) {
    suspend fun getNews(): List<Article> {
        val response = api.getTopHeadlines("us", "dcb08905f38549049c486400be5097c4")
        val articlesToSave = response.articles.ensureUniqueIds()
        newsDao.insertAll(articlesToSave)
        return response.articles
    }

    suspend fun getCachedNews(): List<Article> = newsDao.getAllArticles().firstOrNull() ?: emptyList()
}