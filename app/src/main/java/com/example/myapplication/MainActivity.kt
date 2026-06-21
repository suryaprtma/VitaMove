package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = AuthRepository(this)
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val authViewModel: AuthViewModel = viewModel { AuthViewModel(repository) }
                var isNewUserProcess by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf("auth") }
                var selectedExercise by remember { mutableStateOf("") }
                
                // Track water globally to share between Home and Profile
                var waterCount by remember { mutableIntStateOf(0) }
                
                val currentUser by authViewModel.currentUser.collectAsState()
                val registerState by authViewModel.registerState.collectAsState()

                LaunchedEffect(registerState) {
                    if (registerState is com.example.myapplication.data.model.AuthResult.Success) {
                        isNewUserProcess = true
                    }
                }

                LaunchedEffect(currentUser) {
                    if (currentUser != null && currentScreen == "auth") {
                        currentScreen = "success_login"
                    }
                }

                when (currentScreen) {
                    "auth" -> AuthScreen(authViewModel = authViewModel)

                    "success_login" -> SuccessLoginScreen(
                        onAnimationFinished = { 
                            if (isNewUserProcess) currentScreen = "gender_selection" 
                            else currentScreen = "home"
                        }
                    )

                    "gender_selection" -> GenderSelectionScreen(onNextClick = { currentScreen = "age_selection" })
                    "age_selection" -> AgeSelectionScreen(onNextClick = { currentScreen = "weight_selection" })
                    "weight_selection" -> WeightSelectionScreen(onNextClick = { currentScreen = "height_selection" })
                    "height_selection" -> HeightSelectionScreen(onNextClick = { currentScreen = "home" })

                    "home" -> HomeScreen(
                        userName = currentUser?.fullName ?: "User",
                        waterCount = waterCount,
                        onWaterAdd = { if (waterCount < 8) waterCount++ },
                        onProfileClick = { currentScreen = "profile" },
                        onNotificationClick = { },
                        onStartWorkout = { type -> 
                            selectedExercise = type
                            currentScreen = "workout_detail" 
                        },
                        onExerciseClick = { name -> 
                            selectedExercise = name
                            currentScreen = "workout_detail" 
                        },
                        onProgresClick = { }
                    )

                    "workout_detail" -> WorkoutDetailScreen(
                        exerciseName = selectedExercise,
                        onBackClick = { currentScreen = "home" },
                        onFinishClick = { currentScreen = "home" }
                    )
                    
                    "profile" -> ProfileScreen(
                        userName = currentUser?.fullName ?: "User",
                        userEmail = currentUser?.email ?: "",
                        userBio = "VITA CHAMPION • TITAN RANK",
                        waterCount = waterCount,
                        onProfileUpdate = { _, _, _ -> },
                        onWaterUpdate = { newCount -> waterCount = newCount },
                        onBackClick = { currentScreen = "home" },
                        onLogoutClick = {
                            authViewModel.logout()
                            isNewUserProcess = false
                            currentScreen = "auth"
                        }
                    )
                }
            }
        }
    }
}
