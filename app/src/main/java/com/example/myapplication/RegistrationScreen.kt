package com.example.myapplication

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun RegistrationScreen(
    onRegisterClick: (String, String, String, String, String, String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String = ""
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
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
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 80.dp))
                .background(designerGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 30.dp),
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join Us",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-2).sp
                )
                Text(
                    text = "Mulai transformasi sehatmu bersama VitaMove.",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.85f)
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryPurple,
                unfocusedBorderColor = Color.LightGray.copy(0.5f),
                focusedLabelColor = primaryPurple
            )

            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Nama Lengkap") }, leadingIcon = { Icon(Icons.Outlined.Person, null, tint = primaryPurple) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = fieldColors, singleLine = true)
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, leadingIcon = { Icon(Icons.Outlined.Email, null, tint = primaryPurple) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = fieldColors, singleLine = true)
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Alamat") }, leadingIcon = { Icon(Icons.Outlined.Home, null, tint = primaryPurple) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = fieldColors, singleLine = true)
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Nomor Telepon") }, leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = primaryPurple) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = fieldColors, singleLine = true)
            
            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Password") }, 
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = primaryPurple) },
                trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.Gray) } },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = fieldColors, singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Register Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(designerGradient)
                    .clickable(enabled = !isLoading && fullName.isNotEmpty()) { onRegisterClick(fullName, phone, address, email, password, "") },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("DAFTAR SEKARANG", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sudah memiliki akun? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Masuk",
                    color = primaryPurple,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            // --- SOCIAL LOGIN SECTION ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray.copy(0.5f))
                Text(" or ", color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp), fontSize = 14.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray.copy(0.5f))
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
