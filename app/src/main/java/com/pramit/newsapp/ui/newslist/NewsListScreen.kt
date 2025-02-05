package com.pramit.newsapp.ui.newslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.pramit.newsapp.R
import com.pramit.newsapp.model.Article
import com.pramit.newsapp.viewmodel.NewsViewModel
import com.pramit.newsapp.ui.state.UiState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    navController: NavHostController,
    viewModel: NewsViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "News List", style = MaterialTheme.typography.titleLarge) },
                Modifier.background(MaterialTheme.colorScheme.primary)
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = scaffoldState.snackbarHostState)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            //based on uiState it will execute the code
            when (uiState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {

                    NewsList(
                        articles = (uiState as UiState.Success).articles,
                        onArticleClick = { article ->
                            navController.navigate("news_details_screen/${article.id}")
                        }
                    )
                }
                is UiState.CachedData -> {
                    val cachedState = uiState as UiState.CachedData
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar(cachedState.message)
                    }

                    NewsList(
                        articles = cachedState.articles,
                        onArticleClick = { article ->
                            navController.navigate("news_details_screen/${article.id}")
                        }
                    )
                }
                is UiState.Error -> {
                    val errorState = uiState as UiState.Error
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        scaffoldState.snackbarHostState.showSnackbar("Error: ${errorState.message}")
                    }
                    ErrorScreen(errorState.message) { viewModel.fetchNews() }
                }
            }
        }
    }
}

@Composable
fun NewsList(articles: List<Article>, onArticleClick: (Article) -> Unit) {
    LazyColumn(modifier = Modifier.padding(0.dp, 0.dp,0.dp,0.dp)) {
        items(articles) { article ->
            NewsItem(article = article) { onArticleClick(article) }
        }
    }
}

@Composable
fun NewsItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            val imageUrl = article.urlToImage
            val painter = if (imageUrl != null) {
                rememberImagePainter(data = imageUrl)
            } else {
                painterResource(R.drawable.image_not_available) // Placeholder image
            }

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = article.title, style = MaterialTheme.typography.titleMedium)
                Text(text = article.description ?: "", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

