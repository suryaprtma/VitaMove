package com.example.myapplication

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.myapplication.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    waterCount: Int,
    onWaterAdd: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onStartWorkout: (String) -> Unit,
    onExerciseClick: (String) -> Unit,
    onProgresClick: () -> Unit,
    onWeightCurveClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Beranda") }
    var showNotifications by remember { mutableStateOf(false) }
    var showWeightDetails by remember { mutableStateOf(false) }
    var selectedMovementDetail by remember { mutableStateOf<String?>(null) }
    var showHydrationReminder by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            BottomNavBar(selectedTab, VitaMovePurple, VitaMoveGradientHorizontal) { tab ->
                selectedTab = tab
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
                label = "TabTransition"
            ) { tab ->
                when (tab) {
                    "Beranda" -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                            HeaderSection(
                                userName = userName,
                                onProfileClick = onProfileClick,
                                onNotificationClick = { showNotifications = true }
                            )

                            if (showHydrationReminder && waterCount < 8) {
                                HydrationBanner(VitaMovePurple, onWaterAdd) { showHydrationReminder = false }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            MainWorkoutCard(VitaMoveGradientHorizontal, VitaMovePurple) { onStartWorkout("Full Body VitaMove") }

                            Spacer(modifier = Modifier.height(32.dp))

                            HomeQuickProgress(
                                accentColor = VitaMovePurple,
                                onDetailClick = { selectedTab = "Progres" },
                                onItemClick = { name -> selectedMovementDetail = name }
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            ExerciseCategories(onExerciseClick, VitaMovePurple)

                            Spacer(modifier = Modifier.height(32.dp))
                            WeightCurveSection(VitaMovePurple, VitaMoveBlue) { showWeightDetails = true }

                            Spacer(modifier = Modifier.height(120.dp))
                        }
                    }
                    "Progres" -> {
                        ProgressScreenContent(VitaMovePurple, VitaMoveGradientHorizontal) { name -> selectedMovementDetail = name }
                    }
                    "Profil" -> {
                        ProfileTabContent(
                            userName = userName,
                            onFullProfileClick = onProfileClick,
                            onLogoutClick = {
                                selectedTab = "Beranda"
                                onProfileClick()
                            }
                        )
                    }
                }
            }

            if (showNotifications) NotificationOverlay(VitaMovePurple) { showNotifications = false }
            if (showWeightDetails) WeightDetailOverlay(VitaMovePurple) { showWeightDetails = false }

            selectedMovementDetail?.let { name ->
                MovementDetailOverlay(name, VitaMovePurple) { selectedMovementDetail = null }
            }
        }
    }
}

@Composable
fun ProfileTabContent(
    userName: String,
    onFullProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = SoftPurpleBg,
            border = BorderStroke(3.dp, VitaMovePurple.copy(alpha = 0.3f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Person, null, modifier = Modifier.size(56.dp), tint = VitaMovePurple)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = "VITA CHAMPION", fontSize = 13.sp, color = VitaMovePurple, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onFullProfileClick,
            modifier = Modifier.fillMaxWidth(0.75f).height(52.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VitaMovePurple)
        ) {
            Icon(Icons.Outlined.Person, null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Lihat Profil Lengkap", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun HydrationBanner(accent: Color, onAdd: () -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = WaterLightBg),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.WaterDrop, null, tint = WaterBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Waktunya Minum!", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Jaga hidrasi untuk performa maksimal.", fontSize = 12.sp, color = Color.Gray)
            }
            TextButton(onClick = onAdd) {
                Text("MINUM", color = WaterBlue, fontWeight = FontWeight.Black)
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            }
        }
    }
}

@Composable
fun WeightCurveSection(accentColor: Color, secondaryColor: Color, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Analisis Berat Badan", fontWeight = FontWeight.Black, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Berat Saat Ini", fontSize = 12.sp, color = Color.Gray)
                        Text("70.5 kg", fontSize = 32.sp, fontWeight = FontWeight.Black, color = accentColor)
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingDown, null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                            Text("-1.2kg", color = SuccessGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val path = Path()
                        path.moveTo(0f, size.height * 0.8f)
                        path.quadraticBezierTo(size.width * 0.2f, size.height * 0.7f, size.width * 0.4f, size.height * 0.5f)
                        path.quadraticBezierTo(size.width * 0.6f, size.height * 0.3f, size.width * 0.8f, size.height * 0.4f)
                        path.lineTo(size.width, size.height * 0.2f)

                        drawPath(
                            path = path,
                            color = accentColor,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )

                        drawCircle(accentColor, 6.dp.toPx(), center = Offset(size.width, size.height * 0.2f))
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Sen", fontSize = 10.sp, color = Color.LightGray)
                    Text("Sel", fontSize = 10.sp, color = Color.LightGray)
                    Text("Rab", fontSize = 10.sp, color = Color.LightGray)
                    Text("Kam", fontSize = 10.sp, color = Color.LightGray)
                    Text("Jum", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HeaderSection(
    userName: String,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .shadow(6.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = VitaMovePurple)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onProfileClick() }
                    .padding(end = 8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Halo,", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                    Text(userName, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }

            Surface(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable { onNotificationClick() },
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Notifications, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun MainWorkoutCard(gradient: Brush, accent: Color, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(200.dp).clickable { onClick() }, shape = RoundedCornerShape(32.dp), elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)) {
        Box(modifier = Modifier.background(gradient).fillMaxSize().padding(28.dp)) {
            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
                Column {
                    Text("LATIHAN HARI INI", color = Color.White.copy(0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Body Power Elite", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
                Surface(color = Color.White, shape = RoundedCornerShape(16.dp)) {
                    Text("MULAI SEKARANG", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), color = accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Icon(Icons.Default.FitnessCenter, null, modifier = Modifier.size(130.dp).align(Alignment.BottomEnd).offset(30.dp, 30.dp).alpha(0.15f), tint = Color.White)
        }
    }
}

@Composable
fun HomeQuickProgress(accentColor: Color, onDetailClick: () -> Unit, onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Progres Hari Ini", fontWeight = FontWeight.Black, fontSize = 20.sp)
            Text("Detail Analisis", color = accentColor, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onDetailClick() })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(modifier = Modifier.fillMaxWidth().shadow(12.dp, RoundedCornerShape(32.dp), spotColor = accentColor.copy(alpha = 0.2f)).clickable { onDetailClick() }, shape = RoundedCornerShape(32.dp), color = Color.White) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                    CircularProgressIndicator(progress = { 0.75f }, color = accentColor, strokeWidth = 6.dp, trackColor = accentColor.copy(alpha = 0.1f), strokeCap = StrokeCap.Round)
                    Text("75%", fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column { Text("Target Warrior", fontWeight = FontWeight.Black, fontSize = 18.sp); Text("Selesaikan 1 sesi lagi!", color = Color.Gray, fontSize = 12.sp) }
                Spacer(modifier = Modifier.weight(1f)); Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun ExerciseCategories(onExerciseClick: (String) -> Unit, accent: Color) {
    val list = listOf("Yoga", "Plank", "Gym", "HIIT")
    Column {
        Text("Kategori", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 24.dp))
        LazyRow(contentPadding = PaddingValues(24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(list) { item ->
                Surface(modifier = Modifier.size(110.dp).clickable { onExerciseClick(item) }, shape = RoundedCornerShape(28.dp), color = accent.copy(alpha = 0.05f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.Bolt, null, tint = accent); Text(item, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressScreenContent(accentColor: Color, gradient: Brush, onActivityClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Statistik VitaMove", fontSize = 32.sp, fontWeight = FontWeight.Black)
        Text("Analisis performa mingguan Anda", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = accentColor) {
            Box(modifier = Modifier.background(gradient).fillMaxWidth().padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Whatshot, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("Streak 12 Hari!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp); Text("Warrior, performa Anda luar biasa.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp) }
                }
            }
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun MovementDetailOverlay(name: String, accent: Color, onDismiss: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.padding(24.dp)) {
            IconButton(onClick = onDismiss) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(32.dp)) }
            Text(name, fontSize = 36.sp, fontWeight = FontWeight.Black, color = accent)
            Spacer(modifier = Modifier.weight(1f)); Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(containerColor = accent)) { Text("KEMBALI", fontWeight = FontWeight.Black) }
        }
    }
}

@Composable
fun NotificationOverlay(accent: Color, onDismiss: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize().clickable { onDismiss() }, color = Color.Black.copy(alpha = 0.6f)) {
        Box(contentAlignment = Alignment.Center) {
            Card(modifier = Modifier.fillMaxWidth(0.85f).padding(24.dp), shape = RoundedCornerShape(32.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Text("Notifikasi", fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Button(onClick = onDismiss, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = accent)) { Text("OKE") }
                }
            }
        }
    }
}

@Composable
fun WeightDetailOverlay(accent: Color, onDismiss: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.padding(24.dp)) {
            IconButton(onClick = onDismiss) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(32.dp)) }
            Text("Analisis Berat", fontSize = 32.sp, fontWeight = FontWeight.Black, color = accent)
        }
    }
}

@Composable
fun BottomNavBar(selected: String, accent: Color, gradient: Brush, onSelected: (String) -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 15.dp) {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            listOf("Beranda", "Progres", "Profil").forEach { tab ->
                val isSelected = selected == tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelected(tab) }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = when (tab) {
                            "Beranda" -> Icons.Default.Home
                            "Progres" -> Icons.AutoMirrored.Filled.ShowChart
                            else -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = if (isSelected) accent else Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                    if (isSelected) {
                        Box(modifier = Modifier.padding(top = 4.dp).size(width = 12.dp, height = 3.dp).clip(CircleShape).background(gradient))
                    }
                }
            }
        }
    }
}
