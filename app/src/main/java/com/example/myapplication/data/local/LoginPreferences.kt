package com.example.myapplication.data.local

import android.content.Context
import androidx.core.content.edit

object LoginPreferences {
    private const val PREFS_NAME = "login_prefs"
    private const val KEY_USER = "key_user"
    private const val KEY_PASS = "key_pass"

    fun saveLogin(context: Context, user: String, pass: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_USER, user)
            putString(KEY_PASS, pass)
        }
    }

    fun getUser(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER, null)

    fun getPass(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PASS, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                clear()
            }
    }

    fun isLogged(context: Context): Boolean =
        getUser(context) != null && getPass(context) != null
}
