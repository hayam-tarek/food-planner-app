package com.example.foodplanner.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_USER_UID = "USER_UID"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserUID(uid: String) {
        sharedPreferences.edit().putString(KEY_USER_UID, uid).apply()
    }

    fun getUserUID(): String? {
        return sharedPreferences.getString(KEY_USER_UID, null)
    }

    fun clearUserUID() {
        sharedPreferences.edit().remove(KEY_USER_UID).apply()
    }
}
