package com.pramit.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.model.Article
import com.pramit.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsDetailsViewmodel @Inject constructor(
    private val newsDao: NewsDao,
) : ViewModel() {

    private val _articleState = MutableStateFlow<Article?>(null)
    val articleState: StateFlow<Article?> = _articleState

    // method to get news article by article id
    fun getArticleById(articleId: String) {
        viewModelScope.launch {
            newsDao.getArticleById(articleId) // This returns a Flow<Article?>
                .catch { e ->
                    e.printStackTrace() // Handle errors gracefully
                }
                .collect { article ->
                    _articleState.value = article
                }
        }
    }
}