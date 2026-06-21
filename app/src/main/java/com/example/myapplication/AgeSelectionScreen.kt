package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.preferences.OnboardingPrefs
import com.example.myapplication.ui.components.OnboardingNextButton
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AgeSelectionScreen(onNextClick: () -> Unit) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 18)
    val coroutineScope = rememberCoroutineScope()
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
            text = "Berapa usia Anda?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            textAlign = TextAlign.Center,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Usia Anda membantu kami menghitung kebutuhan kalori dan intensitas latihan yang paling tepat.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(VitaMovePurple.copy(alpha = 0.05f))
            )

            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
            ) {
                items(100) { index ->
                    val age = index + 1
                    val isSelected = listState.firstVisibleItemIndex == index

                    Text(
                        text = age.toString(),
                        fontSize = if (isSelected) 48.sp else 24.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = if (isSelected) VitaMovePurple else Color.LightGray,
                        modifier = Modifier.clickable {
                            coroutineScope.launch { listState.animateScrollToItem(index) }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OnboardingNextButton {
            prefs.age = listState.firstVisibleItemIndex + 1
            onNextClick()
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
