package com.example.mymedifetchproject.patient

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.shared.Screen
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPatientScreen(
    onNavigate: (String) -> Unit,
    isDarkMode: Boolean
) {
    // --- 1. DYNAMIC THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    val alertBg = if (isDarkMode) Color(0xFF2D1E00) else Color(0xFFFFF3E0)
    val alertText = if (isDarkMode) Color(0xFFFFB74D) else Color(0xFFE65100)

    // --- 2. WORKFLOW LOGIC ---
    val currentProgress = 0.5f
    val (statusInfo, routeInfo) = when {
        currentProgress >= 1.0f -> Triple("Results Ready", "Tap to view prescription.", Icons.Default.AssignmentTurnedIn) to Screen.PatientReports.route
        currentProgress >= 0.5f -> Triple("Lab Test Required", "Doctor requested a Lab Test.", Icons.Default.Science) to Screen.FindLabs.route
        else -> Triple("Reviewing", "Provider is reviewing symptoms.", Icons.Default.HourglassEmpty) to "no_action"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // --- 3. HEADER ---
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(45.dp).background(accentBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("DJ", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("David John", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryText)
                Text("ID: MF-2026-001", color = secondaryText, fontSize = 12.sp)
            }
            BadgedBox(badge = { Badge { Text("1") } }) {
                Icon(Icons.Filled.Notifications, contentDescription = null, tint = primaryText)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. ACTION ALERT ---
        if (currentProgress == 0.5f) {
            Card(
                onClick = { onNavigate(routeInfo) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = alertBg)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = alertText)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Action Required", fontWeight = FontWeight.ExtraBold, color = alertText)
                        Text("A lab test is needed to finish diagnosis.", fontSize = 12.sp, color = primaryText)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- 5. QUICK ACTIONS ---
        Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = primaryText)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            onClick = { onNavigate(Screen.ReportSickness.route) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = accentBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Report Sickness", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Contact provider now", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 6. STATS (FIXED WEIGHT LOGIC) ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Reports",
                count = "1",
                icon = Icons.Filled.Description,
                cardBg = cardBg,
                iconColor = accentBlue,
                primaryText = primaryText,
                secondaryText = secondaryText,
                isDarkMode = isDarkMode,
                modifier = Modifier.weight(1f) // ✅ Passing weight here
            ) {
                onNavigate(Screen.PatientReports.route)
            }
            StatCard(
                title = "Labs Visited",
                count = "12",
                icon = Icons.Filled.Science,
                cardBg = cardBg,
                iconColor = Color(0xFF42A5F5),
                primaryText = primaryText,
                secondaryText = secondaryText,
                isDarkMode = isDarkMode,
                modifier = Modifier.weight(1f) // ✅ Passing weight here
            ) {
                onNavigate(Screen.FindLabs.route)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 7. ACTIVE TRACKER ---
        Text("Active Health Case", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = primaryText)
        Spacer(modifier = Modifier.height(12.dp))
        ActiveCaseTracker(
            illness = "Fever & Shivering",
            status = statusInfo.first,
            progress = currentProgress,
            instructionText = statusInfo.second,
            icon = statusInfo.third,
            themeColor = accentBlue,
            cardBg = cardBg,
            isDarkMode = isDarkMode,
            primaryText = primaryText,
            onTrackClick = { if (routeInfo != "no_action") onNavigate(routeInfo) }
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

// --- SUB-COMPONENTS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(
    title: String,
    count: String,
    icon: ImageVector,
    cardBg: Color,
    iconColor: Color,
    primaryText: Color,
    secondaryText: Color,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier, // ✅ Added modifier parameter
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier, // ✅ Applied modifier here
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = secondaryText, fontSize = 14.sp)
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveCaseTracker(
    illness: String,
    status: String,
    progress: Float,
    instructionText: String,
    icon: ImageVector,
    themeColor: Color,
    cardBg: Color,
    isDarkMode: Boolean,
    primaryText: Color,
    onTrackClick: () -> Unit
) {
    Card(
        onClick = onTrackClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = themeColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(illness, fontWeight = FontWeight.Bold, color = primaryText)
                Spacer(modifier = Modifier.weight(1f))
                Text(status, color = themeColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = themeColor,
                trackColor = themeColor.copy(alpha = 0.2f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(color = themeColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                Text(instructionText, fontSize = 12.sp, color = themeColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun PreviewDashboardLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        DashboardPatientScreen(onNavigate = {}, isDarkMode = false)
    }
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewDashboardDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        DashboardPatientScreen(onNavigate = {}, isDarkMode = true)
    }
}