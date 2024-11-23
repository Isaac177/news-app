package com.example.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "news_articles")
data class NewsArticle(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val publishedAt: Date,
    val imageUrl: String
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
