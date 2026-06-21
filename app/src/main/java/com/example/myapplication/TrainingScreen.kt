package com.example.myapplication

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TrainingScreen(onStartClick: () -> Unit) {
    var animationIndex by remember { mutableIntStateOf(0) }
    val animations = listOf("BERLARI", "GYM", "YOGA")
    val icons = listOf(
        Icons.AutoMirrored.Filled.DirectionsRun, 
        Icons.Default.FitnessCenter, 
        Icons.Default.SelfImprovement
    )

    // Loop animasi setiap 2 detik terus menerus
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            animationIndex = (animationIndex + 1) % animations.size
        }
    }

    val purpleBlueGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6650a4), Color(0xFF1A237E))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Siap berlatih dan olahraga hari ini?",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A237E)
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Area Animasi (Simulasi Video)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color(0xFFF8F9FE), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = animationIndex, animationSpec = tween(1000)) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = null,
                        modifier = Modifier.size(150.dp),
                        tint = Color(0xFF6650a4)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = animations[index],
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1A237E),
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Tombol MULAI dengan gradient purple kebiruan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(purpleBlueGradient, RoundedCornerShape(20.dp))
                .clickable { onStartClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MULAI",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 4.sp
            )
        }
    }
}
