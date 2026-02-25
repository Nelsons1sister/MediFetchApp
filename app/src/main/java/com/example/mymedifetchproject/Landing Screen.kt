package com.example.mymedifetchproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.health),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Stronger Gradient Overlay for better text contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.95f) // Darker bottom
                        ),
                        startY = 450f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            // 3. Branding Icon
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.medical),
                        contentDescription = "Logo",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Headline
            Text(
                text = "MediFetch",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            // 5. Sub-headline
            Text(
                text = "Smart Diagnostics. Faster Recovery.",
                fontSize = 18.sp,
                color = Color(0xFF4DB6AC),
                fontWeight = FontWeight.Bold // Bolded for clarity
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Value Proposition - UPDATED: Bold & Solid White for high legibility
            Text(
                text = "Report your symptoms, get lab-tested, and receive expert prescriptions—all in one place.",
                fontSize = 17.sp, // Slightly larger
                textAlign = TextAlign.Center,
                color = Color.White, // Solid white (no transparency)
                fontWeight = FontWeight.Bold, // Extra emphasis
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // 7. Get Started Button
            Button(
                onClick = { onNavigate() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "GET STARTED",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun LandingScreenPreview() {
    MaterialTheme {
        LandingScreen(onNavigate = {})
    }
}