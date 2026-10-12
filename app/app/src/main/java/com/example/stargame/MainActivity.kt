package com.example.stargame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Game() } }
    }
}

@Composable
fun Game() {
    var score by remember { mutableIntStateOf(0) }
    var best by remember { mutableIntStateOf(0) }
    var time by remember { mutableIntStateOf(30) }
    var running by remember { mutableStateOf(false) }
    var fx by remember { mutableFloatStateOf(0.5f) }
    var fy by remember { mutableFloatStateOf(0.5f) }

    // العدّاد التنازلي
    LaunchedEffect(running) {
        if (running) {
            while (time > 0) {
                delay(1000)
                time--
            }
            running = false
            if (score > best) best = score
        }
    }

    // تنتقل النجمة إن لم تُضغط
    LaunchedEffect(running, score) {
        if (running) {
            delay(1200)
            fx = Random.nextFloat()
            fy = Random.nextFloat()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("النقاط: $score", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("الوقت: $time", color = Color(0xFFFFD54F), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val starSize = 72.dp
            if (running) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = (maxWidth - starSize) * fx,
                            y = (maxHeight - starSize) * fy
                        )
                        .size(starSize)
                        .clickable {
                            score++
                            fx = Random.nextFloat()
                            fy = Random.nextFloat()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("⭐", fontSize = 52.sp)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⭐ صيد النجوم ⭐", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    if (time == 0) {
                        Text("انتهى الوقت! نتيجتك: $score", color = Color.White, fontSize = 20.sp)
                    }
                    Text("أفضل نتيجة: $best", color = Color(0xFFFFD54F), fontSize = 18.sp)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = {
                        score = 0
                        time = 30
                        running = true
                    }) {
                        Text(if (time == 0) "العب مرة أخرى" else "ابدأ اللعب", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}
