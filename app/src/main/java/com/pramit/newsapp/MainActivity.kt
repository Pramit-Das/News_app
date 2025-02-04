package com.pramit.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pramit.newsapp.ui.newsdetails.NewsDetailsScreen
import com.pramit.newsapp.ui.newslist.NewsListScreen
import com.pramit.newsapp.ui.splash.SplashScreen
import com.pramit.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "splash_screen"
            ) {
                // Splash Screen
                composable("splash_screen") {
                    SplashScreen(navController)
                }

                // News List Screen
                composable("news_list_screen") {
                    NewsListScreen(navController ,onArticleClick = { article ->
                        navController.navigate("news_details_screen/${article.id}")
                    })
                }

                // News Details Screen
                composable("news_details_screen/{articleId}") { backStackEntry ->
                    val articleId = backStackEntry.arguments?.getString("articleId")
                    if (articleId != null) {
                        NewsDetailsScreen(navController ,articleId = articleId)
                    }
                }
            }
        }
    }
}
