package com.example.news.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.news.ui.components.LoadingAnimation
import com.example.news.ui.components.NewsItem
import com.example.news.ui.state.UiState

@Composable
fun NewsListScreen(onNewsItemClick: (String) -> Unit) {
    val viewModel: NewsListViewModel = viewModel()
    val articlesState by viewModel.articlesState.collectAsState()

    when (val state = articlesState) {
        is UiState.Loading -> {
            LoadingAnimation()
        }
        is UiState.Success -> {
            LazyColumn {
                items(state.data) { article ->
                    NewsItem(
                        article = article,
                        onItemClick = { onNewsItemClick(article.id) }
                    )
                }
            }
        }
        is UiState.Error -> {
            Text(
                text = "Error: ${state.message}",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
