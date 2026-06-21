package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.model.*
import com.example.myapplication.data.preferences.SessionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay

class AuthRepository(context: Context) {
    private val sessionManager = SessionManager(context)
    private val prefs = context.getSharedPreferences("user_db_permanent", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Mengambil database user dari penyimpanan HP agar tidak hilang
    private fun getUserDatabase(): MutableMap<String, Pair<User, String>> {
        val json = prefs.getString("users_list", null)
        return if (json == null) {
            // Akun admin default untuk masuk cepat
            mutableMapOf(
                "admin@fitnes.com" to (User(uid = "1", fullName = "Admin Fitnes", email = "admin@fitnes.com") to "admin123")
            )
        } else {
            val type = object : TypeToken<MutableMap<String, Pair<User, String>>>() {}.type
            gson.fromJson(json, type)
        }
    }

    private fun saveUserDatabase(db: Map<String, Pair<User, String>>) {
        val json = gson.toJson(db)
        prefs.edit().putString("users_list", json).apply()
    }

    /**
     * LOGIKA LOGIN: Langsung ke Beranda
     */
    suspend fun login(request: LoginRequest): AuthResult<User> {
        delay(1000)
        val email = request.emailOrUsername.trim()
        val password = request.password

        val db = getUserDatabase()
        val entry = db[email] ?: return AuthResult.Error(Exception("Akun tidak ditemukan. Silakan daftar."))
        
        return if (entry.second == password) {
            val user = entry.first
            saveSession(user)
            AuthResult.Success(user)
        } else {
            AuthResult.Error(Exception("Password salah!"))
        }
    }

    /**
     * LOGIKA DAFTAR: Harus isi data kesehatan (Gender, dll)
     */
    suspend fun register(request: RegisterRequest): AuthResult<User> {
        delay(1200)
        val email = request.email.trim()
        val db = getUserDatabase()
        
        if (db.containsKey(email)) {
            return AuthResult.Error(Exception("Email sudah digunakan."))
        }

        val newUser = User(
            uid = "user_${System.currentTimeMillis()}",
            username = email.substringBefore("@"),
            email = email,
            fullName = request.fullName
        )
        
        db[email] = newUser to request.password
        saveUserDatabase(db)
        saveSession(newUser)
        return AuthResult.Success(newUser)
    }

    private fun saveSession(user: User) {
        sessionManager.saveUserSession(user.uid, user.email, user.username, user.fullName)
    }

    fun logout() {
        sessionManager.clearSession()
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        return User(
            uid = sessionManager.getUid() ?: "",
            email = sessionManager.getEmail() ?: "",
            username = sessionManager.getUsername() ?: "",
            fullName = sessionManager.getFullName() ?: ""
        )
    }
}
