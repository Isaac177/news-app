package com.example.news.data

import androidx.room.*
import com.example.news.model.NewsArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE id = :id")
    suspend fun getArticleById(id: String): NewsArticle?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Query("DELETE FROM news_articles")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM news_articles")
    suspend fun getArticleCount(): Int
}
