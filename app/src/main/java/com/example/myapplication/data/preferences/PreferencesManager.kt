package com.example.myapplication.data.preferences

import android.content.Context

class PreferencesManager(context: Context) {

    private val sharedPref = context.getSharedPreferences("MeuAppPrefs", Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        sharedPref.edit().putString("USER_NAME", name).apply()
    }

    fun getUserName(): String? {
        return sharedPref.getString("USER_NAME", null)
    }

    fun clearUserName() {
        sharedPref.edit().remove("USER_NAME").apply()
    }
}