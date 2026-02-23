package com.example.mymedifetchproject



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingScreen(
    onNavigate: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.health),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dark Overlay (Gradient) - This makes the text "pop"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .size(100.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.medical),
                        contentDescription = "Logo",
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text Section
            Text(
                text = "MediFetch",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Your Healthcare, Simplified",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFB0BEC5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Connect with hospitals, labs, and pharmacies. Access your medical records anytime, anywhere.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Get Started Button
            Button(
                onClick = { onNavigate() },
                shape = RoundedCornerShape(50), // Fully rounded
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "GET STARTED",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun LandingScreenPreview() {
    // We wrap it in our app theme if you have one,
    // otherwise MaterialTheme works fine.
    MaterialTheme {
        LandingScreen(
            onNavigate = { /* Do nothing in preview */ },
            onNavigateBack = { /* Do nothing in preview */ }
        )
    }
}