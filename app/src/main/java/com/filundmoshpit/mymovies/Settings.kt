package com.filundmoshpit.mymovies

import android.content.SharedPreferences
import androidx.core.content.edit

class Settings(private val preferences: SharedPreferences) {
    companion object {
        private const val PREFERENCE_DARK_THEME = "dark_theme"
        private const val PREFERENCE_LANGUAGE = "language"

        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_RUSSIAN = "ru"
    }

    var darkTheme: Boolean
        get() = preferences.getBoolean(PREFERENCE_DARK_THEME, false)
        set(value) = preferences.edit { putBoolean(PREFERENCE_DARK_THEME, value) }

    var language: String
        get() = preferences.getString(PREFERENCE_LANGUAGE, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
        set(value) = preferences.edit { putString(PREFERENCE_LANGUAGE, value) }
}
