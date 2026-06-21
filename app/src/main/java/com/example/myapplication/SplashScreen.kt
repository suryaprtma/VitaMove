package com.example.myapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    // Animasi state
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animasi masuk (1.5 detik)
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        
        // Menunggu agar total durasi sekitar 5 detik
        delay(2500)
        
        // Animasi keluar sedikit sebelum transisi
        scale.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(durationMillis = 1000)
        )
        
        onAnimationFinished()
    }

    // Gradient Ungu ke Biru Gelap
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6650a4), // Purple
            Color(0xFF4A148C), // Deep Purple
            Color(0xFF1A237E)  // Dark Blue
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "VitaMove",
            color = Color.White,
            fontSize = 72.sp,
            fontWeight = FontWeight.ExtraBold, // Lebih tebal (ExtraBold)
            fontFamily = FontFamily.Cursive,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}
