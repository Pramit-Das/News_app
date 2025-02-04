package com.pramit.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.model.Article
import com.pramit.newsapp.repository.NewsRepository
import com.pramit.newsapp.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

@HiltViewModel
class NewsViewModel  @Inject constructor(
    private val newsDao: NewsDao,
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _articleState = MutableStateFlow<Article?>(null)
    val articleState: StateFlow<Article?> = _articleState

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

//    fun getArticleById(articleId: String): Article? {
//        return (uiState.value as? UiState.Success)?.articles?.find { it.id == articleId }
//    }

//    fun getArticleById(articleId: String) {
//        viewModelScope.launch {
//            newsDao.getArticleById(articleId)
//                ?.catch { e ->
//                    // Handle error if needed
//                    e.printStackTrace()
//                }
//                ?.collect { article ->
//                    _articleState.value = article
//                }
//        }
//    }

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