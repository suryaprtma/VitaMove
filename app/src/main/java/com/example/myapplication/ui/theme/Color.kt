package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val VitaMovePurple = Color(0xFF6A1B9A)
val VitaMoveDeepPurple = Color(0xFF4A148C)
val VitaMoveBlue = Color(0xFF311B92)

val VitaMoveGradientHorizontal = Brush.linearGradient(
    colors = listOf(VitaMovePurple, VitaMoveDeepPurple, VitaMoveBlue)
)

val VitaMoveGradientVertical = Brush.verticalGradient(
    colors = listOf(VitaMovePurple, VitaMoveDeepPurple, VitaMoveBlue)
)

val VitaMoveGradientDiagonal = Brush.linearGradient(
    colors = listOf(VitaMovePurple, VitaMoveDeepPurple, VitaMoveBlue)
)

val WaterBlue = Color(0xFF2196F3)
val WaterLightBg = Color(0xFFE3F2FD)
val SuccessGreen = Color(0xFF4CAF50)
val ErrorRed = Color(0xFFC62828)
val ErrorLightBg = Color(0xFFFFEBEE)
val CardBg = Color(0xFFF8F9FA)
val SoftPurpleBg = VitaMovePurple.copy(alpha = 0.05f)
