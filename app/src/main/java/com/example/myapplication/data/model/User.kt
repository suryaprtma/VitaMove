package com.example.myapplication.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val fullName: String = "",
    val address: String = "",
    val phone: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null
)

// Request model for registration
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val address: String,
    val phone: String,
    val password: String,
    val confirmPassword: String
)

// Request model for login
data class LoginRequest(
    val emailOrUsername: String,
    val password: String
)

// Response model for auth operations
sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val exception: Exception) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
}
