package com.example.mymedifetchproject.medifetch

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.R
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val letters = "MEDIFETCH".map { it.toString() }

    // 1. Orbital Rotation State
    val infiniteTransition = rememberInfiniteTransition(label = "orbit")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    // 2. 3D Tilt effect (Fixing the previous 'SineEasing' error)
    val tilt by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "tilt"
    )

    // 3. Animation Control States
    var isCollapsing by remember { mutableStateOf(false) }
    var showWelcome by remember { mutableStateOf(false) }

    val radius by animateDpAsState(
        targetValue = if (isCollapsing) 0.dp else 120.dp,
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "radius"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (isCollapsing && radius < 15.dp) 1f else 0f,
        animationSpec = tween(800), label = "logoAlpha"
    )

    // 4. Animation Sequence
    LaunchedEffect(Unit) {
        delay(3000) // Float and tilt in 3D for 3 seconds
        isCollapsing = true
        delay(1200) // Wait for collapse to finish
        showWelcome = true
        delay(2500) // Display welcome message
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C7B76)), // MediFetch Teal
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {

                // --- 3D ORBITING LETTERS ---
                if (radius > 1.dp) {
                    letters.forEachIndexed { index, letter ->
                        val letterAngle = rotation + (index * (360f / letters.size))
                        val xOffset = with(LocalDensity.current) {
                            (radius.toPx() * cos(Math.toRadians(letterAngle.toDouble()))).toFloat()
                        }
                        val yOffset = with(LocalDensity.current) {
                            (radius.toPx() * sin(Math.toRadians(letterAngle.toDouble()))).toFloat()
                        }

                        Text(
                            text = letter,
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .graphicsLayer(
                                    translationX = xOffset,
                                    translationY = yOffset,
                                    rotationZ = letterAngle + 90f,
                                    rotationX = tilt,
                                    rotationY = tilt / 2,
                                    cameraDistance = 12f * LocalDensity.current.density,
                                    shadowElevation = 10f
                                )
                        )
                    }
                }

                // --- FINAL LOGO REVEAL ---
                Image(
                    painter = painterResource(id = R.drawable.medical1),
                    contentDescription = "MediFetch Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .graphicsLayer(
                            alpha = logoAlpha,
                            scaleX = logoAlpha,
                            scaleY = logoAlpha
                        )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- WELCOME MESSAGE WITH SLIDE TRANSITION ---
            AnimatedVisibility(
                visible = showWelcome,
                enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WELCOME TO MEDIFETCH",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Your Health, Our Priority",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "3D Orbiting Preview")
@Composable
fun SplashScreenOrbitPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2C7B76)),
        contentAlignment = Alignment.Center
    ) {
        val letters = "MEDIFETCH".map { it.toString() }
        letters.forEachIndexed { index, letter ->
            val angle = index * (360f / letters.size)
            val x = (120 * cos(Math.toRadians(angle.toDouble()))).toFloat()
            val y = (120 * sin(Math.toRadians(angle.toDouble()))).toFloat()
            Text(
                text = letter,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.graphicsLayer(
                    translationX = x * 3, // Multiplied for preview visibility
                    translationY = y * 3,
                    rotationZ = angle + 90f,
                    rotationX = 25f,
                    rotationY = 12f
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Logo & Welcome Message Preview")
@Composable
fun SplashScreenFinalPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2C7B76)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.medical1),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "WELCOME TO MEDIFETCH",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Your Health, Our Priority",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}