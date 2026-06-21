package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenderSelectionScreen(onNextClick: () -> Unit) {
    var selectedGender by remember { mutableStateOf("") }

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
            text = "Apa jenis kelamin anda?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            textAlign = TextAlign.Center,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ini akan membantu kami menyesuaikan latihan Anda agar sesuai dengan tingkat metabolisme Anda secara sempurna.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderCard(
                label = "Laki-laki",
                icon = Icons.Default.Male,
                isSelected = selectedGender == "male",
                activeColor = primaryPurple,
                modifier = Modifier.weight(1f),
                onClick = { selectedGender = "male" }
            )
            GenderCard(
                label = "Perempuan",
                icon = Icons.Default.Female,
                isSelected = selectedGender == "female",
                activeColor = Color(0xFFE91E63),
                modifier = Modifier.weight(1f),
                onClick = { selectedGender = "female" }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (selectedGender.isNotEmpty()) designerGradient else Brush.linearGradient(listOf(Color.LightGray, Color.LightGray)))
                .clickable(enabled = selectedGender.isNotEmpty()) { onNextClick() },
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

@Composable
fun GenderCard(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) activeColor.copy(alpha = 0.05f) else Color(0xFFF8F9FE)
    val borderColor = if (isSelected) activeColor else Color.Transparent

    Box(
        modifier = modifier
            .height(200.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(32.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(if (isSelected) activeColor else Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (isSelected) Color.White else Color.LightGray
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) activeColor else Color.Gray
            )
        }
    }
}
