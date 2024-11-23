package com.example.news.data

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("news_preferences", Context.MODE_PRIVATE)

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK_THEME, false)
        set(value) = prefs.edit { putBoolean(KEY_DARK_THEME, value) }

    var lastUpdateTimestamp: Long
        get() = prefs.getLong(KEY_LAST_UPDATE, 0L)
        set(value) = prefs.edit { putLong(KEY_LAST_UPDATE, value) }

    var articleCount: Int
        get() = prefs.getInt(KEY_ARTICLE_COUNT, 0)
        set(value) = prefs.edit { putInt(KEY_ARTICLE_COUNT, value) }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_LAST_UPDATE = "last_update"
        private const val KEY_ARTICLE_COUNT = "article_count"
    }
}
