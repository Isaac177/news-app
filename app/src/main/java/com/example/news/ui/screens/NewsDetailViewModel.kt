package com.example.news.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.NewsRepository
import com.example.news.model.NewsArticle
import com.example.news.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewsRepository.getInstance(application, viewModelScope)
    
    private val _articleState = MutableStateFlow<UiState<NewsArticle>>(UiState.Loading)
    val articleState: StateFlow<UiState<NewsArticle>> = _articleState

    fun getArticle(id: String) {
        viewModelScope.launch {
            _articleState.value = UiState.Loading
            try {
                val result = repository.getNewsArticle(id)
                _articleState.value = if (result != null) {
                    UiState.Success(result)
                } else {
                    UiState.Error("Article not found")
                }
            } catch (e: Exception) {
                _articleState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
