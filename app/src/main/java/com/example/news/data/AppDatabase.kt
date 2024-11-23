package com.example.news.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.news.model.Converters
import com.example.news.model.NewsArticle
import kotlinx.coroutines.CoroutineScope

@Database(entities = [NewsArticle::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "news_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseSeeder.createCallback(context, scope, null))
                    .build()
                    .also { database ->
                        INSTANCE = database
                        database.callback = DatabaseSeeder.createCallback(context, scope, database)
                    }
                instance
            }
        }
    }

    var callback: RoomDatabase.Callback? = null
}