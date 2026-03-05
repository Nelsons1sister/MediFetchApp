package com.example.mymedifetchproject.medifetch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.R
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun ChooseAccountTypeScreen(
    isDarkMode: Boolean = false,
    onBack: () -> Unit = {},
    onRoleSelected: (String) -> Unit
) {
    // --- 1. DYNAMIC COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE3F2FD)
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color(0xFF0D47A1)
    val secondaryText = if (isDarkMode) Color.LightGray else Color(0xFF1976D2).copy(alpha = 0.7f)
    val iconContainerColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFE3F2FD)
    val backBtnBg = if (isDarkMode) Color(0xFF2C2C2C) else Color.White

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
            .statusBarsPadding()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. BACK BUTTON ---
        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .background(backBtnBg, CircleShape)
                .size(45.dp)
                .shadow(if (isDarkMode) 0.dp else 2.dp, CircleShape)
        ) {
            Text(text = "←", fontSize = 24.sp, color = primaryText, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 3. HEADER ---
        Text(
            text = "Continue as...",
            color = primaryText,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose your account type to get started",
            color = secondaryText,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- 4. ACCOUNT TYPE CARDS ---

        // PATIENT
        AccountOptionCard(
            isDarkMode = isDarkMode,
            icon = R.drawable.medical1,
            title = "Patient",
            description = "Book appointments, report symptoms, and track records.",
            onClick = { onRoleSelected("patient") },
            primaryText = primaryText,
            cardBg = cardBg,
            iconBg = iconContainerColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SERVICE PROVIDER (Doctor/Clinic)
        AccountOptionCard(
            isDarkMode = isDarkMode,
            icon = R.drawable.medical1,
            title = "Service Provider",
            description = "Manage lab results, prescriptions, and patient care.",
            onClick = { onRoleSelected("provider") },
            primaryText = primaryText,
            cardBg = cardBg,
            iconBg = iconContainerColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        // LAB TECHNICIAN
        AccountOptionCard(
            isDarkMode = isDarkMode,
            icon = R.drawable.medical1,
            title = "Lab Technician",
            description = "Process samples, upload results, and manage lab queue.",
            onClick = { onRoleSelected("labtech") },
            primaryText = primaryText,
            cardBg = cardBg,
            iconBg = iconContainerColor
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AccountOptionCard(
    isDarkMode: Boolean,
    icon: Int,
    title: String,
    description: String,
    primaryText: Color,
    cardBg: Color,
    iconBg: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .shadow(if (isDarkMode) 4.dp else 12.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(iconBg, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF1976D2),
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = primaryText
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )
            }
        }
    }
}

// --- DUAL PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ChooseAccountTypeLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ChooseAccountTypeScreen(isDarkMode = false, onRoleSelected = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ChooseAccountTypeDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ChooseAccountTypeScreen(isDarkMode = true, onRoleSelected = {})
    }
}