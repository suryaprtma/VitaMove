package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.AuthResult
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.data.model.User
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Login state
    private val _loginState = MutableStateFlow<AuthResult<User>>(AuthResult.Loading)
    val loginState: StateFlow<AuthResult<User>> = _loginState

    // Register state
    private val _registerState = MutableStateFlow<AuthResult<User>>(AuthResult.Loading)
    val registerState: StateFlow<AuthResult<User>> = _registerState

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    // Current user
    private val _currentUser = MutableStateFlow<User?>(authRepository.getCurrentUser())
    val currentUser: StateFlow<User?> = _currentUser

    fun register(
        fullName: String,
        email: String,
        address: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = RegisterRequest(
                    fullName = fullName.trim(),
                    email = email.trim(),
                    address = address.trim(),
                    phone = phone.trim(),
                    password = password,
                    confirmPassword = confirmPassword
                )
                val result = authRepository.register(request)
                _registerState.value = result

                when (result) {
                    is AuthResult.Success -> {
                        _currentUser.value = result.data
                        _errorMessage.value = ""
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.exception.message ?: "Registrasi gagal"
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
                _registerState.value = AuthResult.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LoginRequest(
                    emailOrUsername = emailOrUsername.trim(),
                    password = password
                )
                val result = authRepository.login(request)
                _loginState.value = result

                when (result) {
                    is AuthResult.Success -> {
                        _currentUser.value = result.data
                        _errorMessage.value = ""
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.exception.message ?: "Login gagal"
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
                _loginState.value = AuthResult.Error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _currentUser.value = null
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Logout gagal"
            }
        }
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    init {
        _loginState.value = AuthResult.Loading
        _registerState.value = AuthResult.Loading
    }
}
