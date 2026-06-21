package com.example.myapplication

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String = ""
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // --- VITAMOVE DOMINANT PURPLE GRADIENT ---
    val primaryPurple = Color(0xFF6A1B9A) // Purple 800
    val deepPurple = Color(0xFF4A148C)    // Purple 900
    val blueAccent = Color(0xFF311B92)    // Indigo Blue
    val designerGradient = Brush.linearGradient(colors = listOf(primaryPurple, deepPurple, blueAccent))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- PREMIUM BRANDED HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomStart = 100.dp))
                .background(designerGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Clearer VitaMove Branding
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)) {
                            append("Vita")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Light, letterSpacing = 1.sp)) {
                            append("Move")
                        }
                    },
                    fontSize = 32.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selamat\nDatang",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 52.sp,
                    letterSpacing = (-2).sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Masuk untuk melanjutkan hidup sehatmu bersama VitaMove.",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // --- FORM SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp)
                .background(Color.White, RoundedCornerShape(32.dp))
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryPurple,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.4f),
                focusedLabelColor = primaryPurple
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email atau Username") },
                leadingIcon = { Icon(Icons.Outlined.Email, null, tint = primaryPurple) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = fieldColors,
                singleLine = true
            )

            Column {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = primaryPurple) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = fieldColors,
                    singleLine = true
                )
                Text(
                    text = "Lupa password?",
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp).clickable { },
                    color = deepPurple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Login Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(designerGradient)
                    .clickable(enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()) { onLoginClick(email, password) },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("MASUK SEKARANG", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Belum punya akun? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Daftar",
                    color = primaryPurple,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            // --- SOCIAL LOGIN SECTION ---
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
                Text(
                    text = " or ",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
            }

            Text(
                text = "Sign up with Social Networks",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                SocialMediaButton(text = "G", color = Color(0xFFDB4437))
                SocialMediaButton(text = "f", color = Color(0xFF4267B2))
                SocialMediaButton(text = "X", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SocialMediaButton(text: String, color: Color) {
    Surface(
        modifier = Modifier.size(54.dp).clickable { },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)),
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}
