package com.example.myapplication

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ReadyToWorkoutScreen(onStartClick: () -> Unit) {
    val workoutItems = listOf(
        WorkoutItem("Push Up", Icons.Default.AccessibilityNew),
        WorkoutItem("Sit Up", Icons.AutoMirrored.Filled.DirectionsRun),
        WorkoutItem("Gym Rumahan", Icons.Default.FitnessCenter),
        WorkoutItem("Yoga & Meditasi", Icons.Default.SelfImprovement)
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    // Logika pergantian otomatis setiap 2.5 detik
    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            currentIndex = (currentIndex + 1) % workoutItems.size
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A148C), Color(0xFF1A237E))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sudah siap berlatih dan\nberolahraga hari ini?",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Container Animasi (Cycling Display)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = workoutItems[currentIndex],
                    animationSpec = tween(1000),
                    label = "WorkoutAnim"
                ) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(160.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.12f),
                            shadowElevation = 0.dp
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name,
                                modifier = Modifier
                                    .padding(35.dp)
                                    .fillMaxSize(),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = item.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                Text(
                    text = "MULAI SEKARANG",
                    color = Color(0xFF1A237E),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

data class WorkoutItem(val name: String, val icon: ImageVector)
