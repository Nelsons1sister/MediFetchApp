package com.example.mymedifetchproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChooseAccountTypeScreen(
    onBack: () -> Unit = {},
    onRoleSelected: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.health),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay to make sure white text is readable against the background
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 2. Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBack() }
            ) {
                Text(text = "←", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Back", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 3. Header Text
            Text(
                text = "Continue as...",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Choose your account type to get started",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 4. Patient Card
            AccountOptionCard(
                icon = android.R.drawable.ic_menu_info_details,
                title = "Patient",
                description = "Access your medical records and find healthcare providers",
                onClick = { onRoleSelected("patient") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Service Provider Card
            AccountOptionCard(
                icon = android.R.drawable.ic_menu_manage,
                title = "Service Provider",
                description = "Manage your facility and connect with patients",
                onClick = { onRoleSelected("provider") }
            )
        }
    }
}

@Composable
fun AccountOptionCard(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFE0EDED), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Using icon directly from resources
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color(0xFF2C7B76),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}

// --- PREVIEW SECTION ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChooseAccountTypePreview() {
    ChooseAccountTypeScreen(
        onBack = {},
        onRoleSelected = {}
    )
}