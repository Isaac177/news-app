package com.example.news

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.news.ui.screens.NewsDetailScreen
import com.example.news.ui.screens.NewsListScreen
import com.example.news.ui.screens.SplashScreen
import com.example.news.data.AppDatabase
import com.example.newsapp.ui.theme.NewsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppDatabase.getDatabase(applicationContext, scope)

        setContent {
            NewsTheme {
                NewsApp()
            }
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen {
                        navController.navigate("newsList") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
                composable("newsList") {
                    NewsListScreen(
                        onNewsItemClick = { newsId ->
                            navController.navigate("newsDetail/$newsId")
                        }
                    )
                }
                composable(
                    route = "newsDetail/{newsId}",
                    arguments = listOf(navArgument("newsId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val newsId = backStackEntry.arguments?.getString("newsId")
                    NewsDetailScreen(newsId = newsId)
                }
            }
        }
    }
}
