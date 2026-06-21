package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun HeightSelectionScreen(onNextClick: () -> Unit) {
    var height by remember { mutableFloatStateOf(170f) }
    
    // --- VITAMOVE DOMINANT PURPLE GRADIENT ---
    val primaryPurple = Color(0xFF6A1B9A) // Purple 800
    val deepPurple = Color(0xFF4A148C)    // Purple 900
    val blueAccent = Color(0xFF311B92)    // Indigo Blue
    val designerGradient = Brush.linearGradient(colors = listOf(primaryPurple, deepPurple, blueAccent))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Berapa tinggi badan Anda?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            textAlign = TextAlign.Center,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tinggi badan diperlukan untuk menghitung BMI Anda secara presisi.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = height.roundToInt().toString(),
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Black,
                        color = primaryPurple
                    )
                    Text(
                        text = "cm",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryPurple,
                        modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(60.dp))

            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            val newHeight = height + (delta / 5f)
                            if (newHeight in 100f..250f) {
                                height = newHeight
                            }
                        }
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = size.height / 2
                    val spacing = 15.dp.toPx()
                    
                    for (i in -30..30) {
                        val currentTick = height.roundToInt() + i
                        if (currentTick in 100..250) {
                            val y = center + (i * spacing) - ((height % 1) * spacing)
                            val width = if (currentTick % 5 == 0) 40.dp.toPx() else 20.dp.toPx()
                            val thickness = if (currentTick % 5 == 0) 3.dp.toPx() else 1.5.dp.toPx()
                            val alpha = 1f - (kotlin.math.abs(y - center) / center)

                            drawLine(
                                color = primaryPurple.copy(alpha = alpha.coerceIn(0f, 1f)),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = thickness
                            )
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(60.dp)
                        .clip(CircleShape)
                        .background(blueAccent)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- PREMIUM GRADIENT BUTTON ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(designerGradient)
                .clickable { onNextClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LANJUTKAN",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
