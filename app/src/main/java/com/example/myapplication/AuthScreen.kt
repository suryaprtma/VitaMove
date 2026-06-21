package com.example.myapplication

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {
    var isLoginScreen by remember { mutableStateOf(true) }
    
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    // LATAR BELAKANG PUTIH BERSIH
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        AnimatedContent(
            targetState = isLoginScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "AuthTransition"
        ) { showLogin ->
            if (showLogin) {
                LoginScreen(
                    onLoginClick = { email, password -> authViewModel.login(email, password) },
                    onRegisterClick = { 
                        authViewModel.clearErrorMessage()
                        isLoginScreen = false 
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            } else {
                RegistrationScreen(
                    onRegisterClick = { name, phone, address, email, pass, confirm ->
                        authViewModel.register(name, email, address, phone, pass, confirm)
                    },
                    onLoginClick = { 
                        authViewModel.clearErrorMessage()
                        isLoginScreen = true 
                    },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }
}
