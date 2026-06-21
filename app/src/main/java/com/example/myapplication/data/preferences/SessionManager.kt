package com.example.myapplication.data.preferences

import android.content.Context

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "user_session",
        Context.MODE_PRIVATE
    )

    fun saveUserSession(uid: String, email: String, username: String, fullName: String) {
        sharedPreferences.edit().apply {
            putString("uid", uid)
            putString("email", email)
            putString("username", username)
            putString("fullName", fullName)
            putBoolean("isLoggedIn", true)
            commit() // Simpan instan
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
    fun getUid(): String? = sharedPreferences.getString("uid", null)
    fun getEmail(): String? = sharedPreferences.getString("email", null)
    fun getUsername(): String? = sharedPreferences.getString("username", null)
    fun getFullName(): String? = sharedPreferences.getString("fullName", null)

    fun clearSession() {
        sharedPreferences.edit().clear().commit() // Hapus instan
    }
}
