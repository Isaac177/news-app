package com.example.news.data

import android.content.Context
import android.util.Log
import com.example.news.model.NewsArticle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope

class NewsRepository private constructor(
    context: Context,
    scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val database = AppDatabase.getDatabase(context, scope)
    private val newsDao = database.newsDao()
    private val preferencesManager = PreferencesManager(context)

    suspend fun initializeData() = withContext(dispatcher) {
        try {
            if (preferencesManager.articleCount == 0) {
                val mockArticles = MockData.getNewsArticles()
                newsDao.insertAll(mockArticles)
                preferencesManager.articleCount = mockArticles.size
                preferencesManager.lastUpdateTimestamp = System.currentTimeMillis()
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error initializing data", e)
            throw e
        }
    }

    fun getNewsArticles(): Flow<List<NewsArticle>> = newsDao.getAllArticles()
        .flowOn(dispatcher)
        .catch { exception ->
            Log.e("NewsRepository", "Error fetching articles", exception)
            throw exception
        }

    suspend fun getNewsArticle(id: String): NewsArticle? = withContext(dispatcher) {
        try {
            Log.d("NewsRepository", "Searching for article with id: $id")
            val article = newsDao.getArticleById(id)
            Log.d("NewsRepository", "Found article: ${article?.title}")
            article
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error fetching article with id: $id", e)
            throw e
        }
    }

    suspend fun insertArticle(article: NewsArticle) = withContext(dispatcher) {
        try {
            newsDao.insertAll(listOf(article))
            preferencesManager.articleCount = (newsDao.getAllArticles().firstOrNull()?.size ?: 0)
            preferencesManager.lastUpdateTimestamp = System.currentTimeMillis()
        } catch (e: Exception) {
            Log.e("NewsRepository", "Error inserting article", e)
            throw e
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(context: Context, scope: CoroutineScope): NewsRepository {
            return instance ?: synchronized(this) {
                instance ?: NewsRepository(context, scope).also { instance = it }
            }
        }
    }
}
