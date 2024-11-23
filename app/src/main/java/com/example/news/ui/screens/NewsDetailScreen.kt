package com.example.news.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news.ui.components.LoadingAnimation
import com.example.news.ui.components.NewsDetail
import com.example.news.ui.state.UiState

@Composable
fun NewsDetailScreen(newsId: String?) {
    val viewModel: NewsDetailViewModel = viewModel()
    val articleState by viewModel.articleState.collectAsState()

    LaunchedEffect(newsId) {
        newsId?.let { viewModel.getArticle(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = articleState) {
            is UiState.Loading -> {
                LoadingAnimation(modifier = Modifier.fillMaxSize())
            }
            is UiState.Success -> {
                NewsDetail(article = state.data)
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
