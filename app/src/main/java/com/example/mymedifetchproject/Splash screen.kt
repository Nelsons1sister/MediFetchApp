package com.example.mymedifetchproject.medifetch

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    var startLines by remember { mutableStateOf(false) }
    var showLogo by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    // 1. THE LINE RISE: Rises from bottom to upper-mid
    val lineRiseOffset by animateFloatAsState(
        targetValue = if (startLines) -300f else 1200f,
        animationSpec = tween(durationMillis = 1500, easing = EaseOutQuart),
        label = "lineRise"
    )

    // 2. THE LOGO POP: Scaled down to 120dp for a "Local Icon" feel
    val logoRotation by animateFloatAsState(
        targetValue = if (showLogo) 360f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "logoSpin"
    )
    val logoScale by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoPop"
    )

    LaunchedEffect(Unit) {
        delay(300)
        startLines = true
        delay(1300)      // Wait for lines to reach position
        showLogo = true  // Logo pops out (Smaller, cleaner)
        delay(800)
        showText = true  // Welcome text appears
        delay(2500)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)), // Clean Medical Light Blue
        contentAlignment = Alignment.Center
    ) {
        // --- STAGE 1: THE RISING RUNWAY LINES ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(translationY = lineRiseOffset),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .height(160.dp)
                            .alpha(0.3f)
                            .background(Color(0xFF1976D2), CircleShape)
                    )
                }
            }
        }

        // --- STAGE 2: THE LOGO & TEXT ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // REFINED LOGO SIZE (120.dp instead of 210.dp)
            Image(
                painter = painterResource(id = R.drawable.medifetch_logo),
                contentDescription = "MediFetch Logo",
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer(
                        scaleX = logoScale,
                        scaleY = logoScale,
                        rotationZ = logoRotation,
                        alpha = if (showLogo) 1f else 0f
                    )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // --- STAGE 3: THE WELCOME TEXT ---
            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { 20 }),
                exit = fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WELCOME TO MEDIFETCH",
                        color = Color(0xFF0D47A1),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Your Health, Our Priority",
                        color = Color(0xFF1565C0).copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalPolishedSplashPreview() {
    SplashScreen(onAnimationFinished = {})
}