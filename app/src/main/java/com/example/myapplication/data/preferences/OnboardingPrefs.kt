package com.example.myapplication.data.preferences

import android.content.Context

class OnboardingPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)

    var gender: String
        get() = prefs.getString("gender", "") ?: ""
        set(value) = prefs.edit().putString("gender", value).apply()

    var age: Int
        get() = prefs.getInt("age", 0)
        set(value) = prefs.edit().putInt("age", value).apply()

    var weight: Float
        get() = prefs.getFloat("weight", 0f)
        set(value) = prefs.edit().putFloat("weight", value).apply()

    var height: Float
        get() = prefs.getFloat("height", 0f)
        set(value) = prefs.edit().putFloat("height", value).apply()

    val isComplete: Boolean
        get() = gender.isNotEmpty() && age > 0 && weight > 0f && height > 0f
}
