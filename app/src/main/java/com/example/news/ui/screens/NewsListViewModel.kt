package com.example.news.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.NewsRepository
import com.example.news.model.NewsArticle
import com.example.news.ui.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsRepository.getInstance(application, viewModelScope)
    
    val articlesState: StateFlow<UiState<List<NewsArticle>>> = repository.getNewsArticles()
        .map { UiState.Success(it) as UiState<List<NewsArticle>> }
        .catch { emit(UiState.Error(it.message ?: "Unknown error occurred")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    init {
        viewModelScope.launch {
            try {
                repository.initializeData()
            } catch (e: Exception) {
                // Initialization error will be caught by the Flow
            }
        }
    }
}
