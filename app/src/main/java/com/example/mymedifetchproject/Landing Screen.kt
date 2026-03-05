package com.example.mymedifetchproject.medifetch

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.R

// 1. Data model for the Carousel Items
data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LandingScreen(onNavigate: () -> Unit = {}) {

    // 2. Updated Data with specific images for Patients, Labs, and Providers
    val onboardingPages = listOf(
        OnboardingItem(
            title = "For Patients",
            description = "Report symptoms instantly and track your health recovery with precision.",
            imageRes = R.drawable.patient // Replace with your saved image name
        ),
        OnboardingItem(
            title = "For Lab Technicians",
            description = "Seamlessly manage lab tests and deliver digital results to patients faster.",
            imageRes = R.drawable.laptest// Replace with your saved image name
        ),
        OnboardingItem(
            title = "For Health Providers",
            description = "Review patient histories and issue expert prescriptions in one unified platform.",
            imageRes = R.drawable.serviceproviderpart// Replace with your saved image name
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)) // Matches Splash Screen Light Blue
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // 3. Branding Header
        Text(
            text = "MediFetch",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0D47A1),
            letterSpacing = 1.sp
        )

        Text(
            text = "Smart Diagnostics Interface",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2).copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.weight(0.5f))

        // 4. THE FLOATING CAROUSEL
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 16.dp
        ) { page ->
            OnboardingCard(item = onboardingPages[page])
        }

        // 5. PAGER INDICATORS (Dots)
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(onboardingPages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF0D47A1) else Color(0xFFBBDEFB)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 6. GET STARTED BUTTON
        Button(
            onClick = { onNavigate() },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(60.dp)
                .shadow(12.dp, RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "GET STARTED",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun OnboardingCard(item: OnboardingItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .shadow(15.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // High-Quality 3D Image Section
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Text Content
            Text(
                text = item.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0D47A1),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.description,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun LandingScreenCarouselPreview() {
    LandingScreen()
}