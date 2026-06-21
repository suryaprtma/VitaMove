package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.model.*
import com.example.myapplication.data.preferences.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val sessionManager = SessionManager(context)

    suspend fun login(request: LoginRequest): AuthResult<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(
                request.emailOrUsername.trim(),
                request.password
            ).await()

            val uid = authResult.user?.uid
                ?: return AuthResult.Error(Exception("Gagal mendapatkan data user"))

            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return AuthResult.Error(Exception("Data user tidak ditemukan"))

            saveSession(user)
            AuthResult.Success(user)
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("There is no user record") == true ->
                    "Akun tidak ditemukan. Silakan daftar."
                e.message?.contains("The password is invalid") == true ->
                    "Password salah!"
                e.message?.contains("email address is badly formatted") == true ->
                    "Format email tidak valid"
                e.message?.contains("The user may have been deleted") == true ->
                    "Akun tidak ditemukan"
                else -> e.message ?: "Login gagal"
            }
            AuthResult.Error(Exception(message))
        }
    }

    suspend fun register(request: RegisterRequest): AuthResult<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                request.email.trim(),
                request.password
            ).await()

            val uid = authResult.user?.uid
                ?: return AuthResult.Error(Exception("Registrasi gagal"))

            val user = User(
                uid = uid,
                username = request.email.trim().substringBefore("@"),
                email = request.email.trim(),
                fullName = request.fullName.trim(),
                address = request.address.trim(),
                phone = request.phone.trim()
            )

            firestore.collection("users").document(uid)
                .set(user, SetOptions.merge())
                .await()

            authResult.user?.updateProfile(
                userProfileChangeRequest {
                    displayName = request.fullName.trim()
                }
            )?.await()

            saveSession(user)
            AuthResult.Success(user)
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("The email address is already in use") == true ->
                    "Email sudah digunakan."
                e.message?.contains("The email address is badly formatted") == true ->
                    "Format email tidak valid"
                e.message?.contains("The given password is weak") == true ->
                    "Password terlalu lemah, minimal 6 karakter"
                e.message?.contains("The email address is already in use by another account") == true ->
                    "Email sudah terdaftar"
                else -> e.message ?: "Registrasi gagal"
            }
            AuthResult.Error(Exception(message))
        }
    }

    suspend fun getCurrentUserFromFirestore(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return try {
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            userDoc.toObject(User::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null || sessionManager.isLoggedIn()
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            return User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                username = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "",
                fullName = firebaseUser.displayName ?: ""
            )
        }
        if (sessionManager.isLoggedIn()) {
            return User(
                uid = sessionManager.getUid() ?: "",
                email = sessionManager.getEmail() ?: "",
                username = sessionManager.getUsername() ?: "",
                fullName = sessionManager.getFullName() ?: ""
            )
        }
        return null
    }

    private fun saveSession(user: User) {
        sessionManager.saveUserSession(user.uid, user.email, user.username, user.fullName)
    }
}
