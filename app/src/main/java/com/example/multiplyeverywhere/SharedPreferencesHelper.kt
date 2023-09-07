package com.example.multiplyeverywhere

import android.content.Context

class SharedPreferencesHelper(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("MultiplyAppSettings", Context.MODE_PRIVATE)

    fun setUserName(userName: String) {
        sharedPreferences.edit().putString("user_name", userName).apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString("user_name", "") ?: ""
    }
    fun getLanguage(): String {
        return sharedPreferences.getString("language", "") ?: ""
    }
    fun setLanguage(language: String) {
        sharedPreferences.edit().putString("language", language).apply()
    }
    fun getTheme(): String {
        return sharedPreferences.getString("theme", "") ?: ""
    }
    fun setTheme(theme: String) {
        sharedPreferences.edit().putString("theme", theme).apply()
    }

}