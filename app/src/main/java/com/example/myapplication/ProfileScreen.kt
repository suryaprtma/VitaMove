package com.example.myapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    userBio: String,
    waterCount: Int,
    onProfileUpdate: (String, String, String) -> Unit,
    onWaterUpdate: (Int) -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    // --- VITAMOVE DOMINANT PURPLE GRADIENT ---
    val primaryPurple = Color(0xFF6A1B9A) // Purple 800
    val deepPurple = Color(0xFF4A148C)    // Purple 900
    val blueAccent = Color(0xFF311B92)    // Indigo Blue
    val designerGradient = Brush.verticalGradient(colors = listOf(primaryPurple, deepPurple, blueAccent))
    
    var isEditing by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(userName) }
    var editEmail by remember { mutableStateOf(userEmail) }
    var editBio by remember { mutableStateOf(userBio) }

    Scaffold(containerColor = Color.White) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER SECTION (IMMERSIVE GRADIENT) ---
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(designerGradient, RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    
                    Text("Profil Saya", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    Row {
                        IconButton(onClick = {
                            if (isEditing) onProfileUpdate(editName, editEmail, editBio)
                            isEditing = !isEditing
                        }) {
                            Icon(if (isEditing) Icons.Default.Check else Icons.Default.Edit, null, tint = Color.White)
                        }
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-10).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 15.dp
                        ) {
                            Box(modifier = Modifier.padding(6.dp).clip(CircleShape).background(primaryPurple.copy(alpha = 0.05f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.size(70.dp), tint = primaryPurple)
                            }
                        }
                        if (isEditing) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(36.dp)
                                    .background(blueAccent, CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                                    .clickable { /* Photo logic simulation */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isEditing) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            modifier = Modifier.width(250.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryPurple)
                        )
                    } else {
                        Text(text = userName, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = userBio, color = primaryPurple, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SMART WATER TRACKER (NOW INTERACTIVE) ---
            CoolWaterIntakeSection(
                count = waterCount, 
                accentColor = Color(0xFF2196F3), 
                onAddWater = { if (waterCount < 8) onWaterUpdate(waterCount + 1) },
                onResetWater = { onWaterUpdate(0) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- INFO SECTION ---
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Informasi Akun", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isEditing) {
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryPurple)
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Status Warrior") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryPurple)
                    )
                } else {
                    ProfileInfoRow(Icons.Default.Email, "Email", userEmail)
                    ProfileInfoRow(Icons.Default.Stars, "Peringkat", userBio)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- STATS GRID ---
            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                ModernProfileCard(Modifier.weight(1f), "Tujuan", "Massa Otot", Icons.Default.EmojiEvents, primaryPurple)
                Spacer(modifier = Modifier.width(16.dp))
                ModernProfileCard(Modifier.weight(1f), "Target", "75 kg", Icons.Default.Flag, primaryPurple)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- LOGOUT BUTTON ---
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFC62828))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Keluar dari Akun", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CoolWaterIntakeSection(count: Int, accentColor: Color, onAddWater: () -> Unit, onResetWater: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text("Target Hidrasi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Ketuk botol untuk menambah air.", fontSize = 12.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$count/8",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor
                )
                if (count > 0) {
                    IconButton(onClick = onResetWater, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Refresh, "Reset", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(accentColor.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                .clickable { onAddWater() }
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(8) { index ->
                val isFilled = index < count
                WaterDropIcon(isFilled, accentColor)
            }
        }
    }
}

@Composable
fun WaterDropIcon(isFilled: Boolean, accentColor: Color) {
    val scale by animateFloatAsState(
        targetValue = if (isFilled) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dropScale"
    )

    Box(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .size(width = 30.dp, height = 45.dp)
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(if (isFilled) accentColor else Color.White)
            .border(
                width = 1.5.dp,
                color = if (isFilled) accentColor else accentColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 8.dp, bottomEnd = 8.dp)
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (isFilled) {
            Icon(
                Icons.Default.Waves,
                null,
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp).padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ModernProfileCard(modifier: Modifier, label: String, value: String, icon: ImageVector, accentColor: Color) {
    Surface(
        modifier = modifier.shadow(12.dp, RoundedCornerShape(24.dp), spotColor = accentColor.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(44.dp).background(accentColor.copy(alpha = 0.05f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Text(label, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 10.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
