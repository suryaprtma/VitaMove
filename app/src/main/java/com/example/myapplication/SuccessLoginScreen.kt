package com.example.myapplication

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SuccessLoginScreen(onAnimationFinished: () -> Unit) {
    var startTextAnim by remember { mutableStateOf(false) }
    
    // --- SIGNATURE VITAMOVE GRADIENT (MATCHING LOGIN SCREEN) ---
    val primaryPurple = Color(0xFF6A1B9A) // Purple 800
    val deepPurple = Color(0xFF4A148C)    // Purple 900
    val blueAccent = Color(0xFF311B92)    // Indigo Blue
    val successGradient = Brush.verticalGradient(
        colors = listOf(primaryPurple, deepPurple, blueAccent)
    )

    LaunchedEffect(Unit) {
        delay(300)
        startTextAnim = true
        delay(2700) 
        onAnimationFinished()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(successGradient), 
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = startTextAnim,
                enter = fadeIn(tween(1000)) + scaleIn(tween(1000), initialScale = 0.8f)
            ) {
                Text(
                    text = "Sudah siap memulai?",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { index ->
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dotAlpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .scale(if (index == 1) pulseScale else 1f)
                            .alpha(dotAlpha)
                            .background(Color.White, CircleShape)
                    )
                }
            }
        }
    }
}
