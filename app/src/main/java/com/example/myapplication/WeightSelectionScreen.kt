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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.preferences.OnboardingPrefs
import com.example.myapplication.ui.components.OnboardingNextButton
import com.example.myapplication.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun WeightSelectionScreen(onNextClick: () -> Unit) {
    var weight by remember { mutableFloatStateOf(70f) }
    val context = LocalContext.current
    val prefs = remember { OnboardingPrefs(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Berapa berat badan Anda?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            textAlign = TextAlign.Center,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ini membantu kami menghitung BMI dan menyesuaikan target kalori harian Anda secara akurat.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = weight.roundToInt().toString(),
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                color = VitaMovePurple
            )
            Text(
                text = "kg",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VitaMovePurple,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newWeight = weight - (delta / 10f)
                        if (newWeight in 30f..200f) {
                            weight = newWeight
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size.width / 2
                val spacing = 20.dp.toPx()

                for (i in -20..20) {
                    val currentTick = weight.roundToInt() + i
                    if (currentTick in 30..200) {
                        val x = center + (i * spacing) - ((weight % 1) * spacing)
                        val height = if (currentTick % 5 == 0) 40.dp.toPx() else 20.dp.toPx()
                        val thickness = if (currentTick % 5 == 0) 3.dp.toPx() else 1.5.dp.toPx()
                        val alpha = 1f - (kotlin.math.abs(x - center) / center)

                        drawLine(
                            color = VitaMovePurple.copy(alpha = alpha.coerceIn(0f, 1f)),
                            start = Offset(x, size.height / 2 - height / 2),
                            end = Offset(x, size.height / 2 + height / 2),
                            strokeWidth = thickness
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(60.dp)
                    .clip(CircleShape)
                    .background(VitaMoveBlue)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingNextButton {
            prefs.weight = weight
            onNextClick()
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
