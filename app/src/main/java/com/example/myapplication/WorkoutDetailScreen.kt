package com.example.myapplication

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun WorkoutDetailScreen(exerciseName: String, onBackClick: () -> Unit, onFinishClick: () -> Unit) {
    var currentSession by remember { mutableIntStateOf(1) }
    val totalSessions = 3
    val sessionDuration = 3 * 60L // 3 Menit dalam detik
    
    var timeLeft by remember { mutableLongStateOf(sessionDuration) }
    var isRunning by remember { mutableStateOf(false) }
    var hasStarted by remember { mutableStateOf(false) }

    val progress = timeLeft.toFloat() / sessionDuration
    val primaryPurple = Color(0xFF673AB7)
    val oceanBlue = Color(0xFF2196F3)
    val gradient = Brush.verticalGradient(listOf(primaryPurple, oceanBlue))

    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0L) {
            isRunning = false
            if (currentSession < totalSessions) {
                // Pindah ke sesi berikutnya otomatis atau beri jeda
                delay(2000L)
                currentSession++
                timeLeft = sessionDuration
            } else {
                onFinishClick()
            }
        }
    }

    Scaffold(containerColor = Color.White) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
                Text(
                    text = exerciseName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = primaryPurple
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- SESSION INDICATOR ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                repeat(totalSessions) { index ->
                    val sessionIndex = index + 1
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (sessionIndex <= currentSession) primaryPurple else Color.LightGray.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Text(
                text = "SESI $currentSession DARI $totalSessions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- TIMER CIRCLE ---
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(280.dp)) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color(0xFFF3E5F5),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = gradient,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val mins = timeLeft / 60
                    val secs = timeLeft % 60
                    Text(
                        text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                    Icon(Icons.Default.Timer, null, tint = primaryPurple, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- CONTROL BUTTONS ---
            if (!hasStarted) {
                // TOMBOL START AWAL
                Button(
                    onClick = { 
                        hasStarted = true
                        isRunning = true 
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryPurple)
                ) {
                    Text("START", fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                }
            } else {
                // TOMBOL PAUSE / RESUME
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { isRunning = !isRunning },
                    shape = CircleShape,
                    color = if (isRunning) Color(0xFFF3E5F5) else primaryPurple,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (isRunning) primaryPurple else Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
