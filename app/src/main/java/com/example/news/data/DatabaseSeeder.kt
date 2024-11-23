package com.example.news.data

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseSeeder {
    companion object {
        private const val TAG = "DatabaseSeeder"

        fun createCallback(
            context: Context,
            scope: CoroutineScope,
            database: AppDatabase?
        ) = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d(TAG, "Database onCreate triggered")
                scope.launch(Dispatchers.IO) {
                    val finalDb = database ?: AppDatabase.getDatabase(context, scope)
                    populateDatabase(finalDb.newsDao())
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "Database onOpen triggered")
                scope.launch(Dispatchers.IO) {
                    val finalDb = database ?: AppDatabase.getDatabase(context, scope)
                    val count = finalDb.newsDao().getArticleCount()
                    Log.d(TAG, "Current article count: $count")
                    if (count == 0) {
                        Log.d(TAG, "Database is empty, populating with initial data")
                        populateDatabase(finalDb.newsDao())
                    }
                }
            }
        }

        private suspend fun populateDatabase(newsDao: NewsDao) {
            try {
                Log.d(TAG, "Starting database population")
                newsDao.deleteAll()
                val articles = MockData.getNewsArticles()
                Log.d(TAG, "Generated ${articles.size} articles")
                newsDao.insertAll(articles)
                val finalCount = newsDao.getArticleCount()
                Log.d(TAG, "Database population completed. Final count: $finalCount")
            } catch (e: Exception) {
                Log.e(TAG, "Error populating database", e)
            }
        }
    }
}