package com.dicoding.storyapp.core.data.local.preference

import android.content.Context

internal class AutoLoginPreference (context: Context) {

    companion object {
        private const val PREFS_NAME = "autologin_pref"
        private const val TOKEN = "token"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getUser(): String? {
        return preferences.getString(TOKEN, "")
    }

}