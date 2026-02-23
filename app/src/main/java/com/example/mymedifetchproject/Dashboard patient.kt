package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.mymedifetchproject.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPatientScreen(
    onNavigate: (String) -> Unit
) {
    // --- SMART LOGIC ENGINE ---
    // Change this to 0.25f (Reviewing), 0.5f (Lab Needed), or 1.0f (Completed)
    val currentProgress = 0.5f

    // Determine UI state based on progress
    val (statusInfo, routeInfo) = when {
        currentProgress >= 1.0f -> {
            Triple(
                "Results Ready",
                "Result: Malaria Positive. Tap to view your prescription.",
                Icons.Default.AssignmentTurnedIn
            ) to Screen.PatientReports.route
        }
        currentProgress >= 0.5f -> {
            Triple(
                "Lab Test Required",
                "Doctor Smith requested a Lab Test. Tap to find a laboratory.",
                Icons.Default.Science
            ) to Screen.FindLabs.route
        }
        else -> {
            Triple(
                "Doctor is Reviewing",
                "Your provider is reviewing your symptoms. Please wait for a request.",
                Icons.Default.HourglassEmpty
            ) to "no_action"
        }
    }

    val (caseStatus, caseInstructions, caseIcon) = statusInfo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // --- PROFILE HEADER ---
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color(0xFF2C7B76), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("DJ", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "David John", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "ID: MF-2026-001", color = Color.Gray, fontSize = 12.sp)
            }
            BadgedBox(badge = { Badge { Text("1") } }) {
                Icon(Icons.Filled.Notifications, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- URGENT ACTION CARD (The "Baton Pass") ---
        if (currentProgress == 0.5f) {
            Card(
                onClick = { onNavigate(Screen.FindLabs.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), // Warning Orange
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE65100))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Action Required", fontWeight = FontWeight.ExtraBold, color = Color(0xFFE65100))
                        Text("A lab test is needed to finish your diagnosis.", fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- QUICK ACTION ---
        Text(text = "Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            onClick = { onNavigate(Screen.ReportSickness.route) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C7B76))
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Report Sickness", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Contact your provider now", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- STATS GRID ---
        Box(modifier = Modifier.height(140.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    StatCard("Reports", "1", Icons.Filled.Description, Color(0xFFE0F2F1), Color(0xFF2C7B76)) {
                        onNavigate(Screen.PatientReports.route)
                    }
                }
                item {
                    StatCard("Labs", "12", Icons.Filled.Science, Color(0xFFE3F2FD), Color(0xFF1E88E5)) {
                        onNavigate(Screen.FindLabs.route)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- ACTIVE CASE TRACKER ---
        Text(text = "Active Health Case", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        ActiveCaseTracker(
            illness = "Fever & Shivering",
            status = caseStatus,
            progress = currentProgress,
            instructionText = caseInstructions,
            icon = caseIcon,
            onTrackClick = {
                if (routeInfo != "no_action") onNavigate(routeInfo)
            }
        )

        Spacer(modifier = Modifier.height(100.dp))
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
    onTrackClick: () -> Unit
) {
    val themeColor = if (progress >= 1.0f) Color(0xFF388E3C) else Color(0xFF2C7B76)

    Card(
        onClick = onTrackClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = themeColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = illness, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = status, color = themeColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = themeColor,
                trackColor = themeColor.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = themeColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = instructionText,
                    fontSize = 12.sp,
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(title: String, count: String, icon: ImageVector, bgColor: Color, iconColor: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(40.dp).background(bgColor, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, color = Color.Gray, fontSize = 14.sp)
            Text(text = count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPatientPreview() {
    DashboardPatientScreen(onNavigate = {})
}