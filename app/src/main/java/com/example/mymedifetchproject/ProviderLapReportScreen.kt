package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

// --- 0. GLOBAL DATA MODEL ---
// IMPORTANT: Delete any other 'data class LabReportItem' in DashboardServiceProvider.kt
// to avoid "Duplicate Class" errors.
data class LabReportItem(
    val patientId: String,
    val patientName: String,
    val testName: String,
    val resultSummary: String,
    val date: String
)

@Composable
fun ProviderLabReportsScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onAction: (String) -> Unit // Renamed to 'onAction' to sync with Dashboard
) {
    // --- Dashboard Synced Palette ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.Gray else Color.DarkGray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White

    // Mock data for the Provider's Inbox
    val completedReports = listOf(
        LabReportItem("P-001", "David John", "Malaria RDT", "Parasite Detected: ++", "Today, 11:30 AM"),
        LabReportItem("P-045", "Sarah Adams", "Full Blood Count", "Normal Range", "Yesterday"),
        LabReportItem("P-089", "Michael Obi", "Typhoid Widal", "Reactive 1:160", "2 days ago")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(top = 16.dp)
            .padding(horizontal = 20.dp)
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = accentBlue
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Lab Results Inbox",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = primaryText
            )
        }

        // --- SECTION LABEL ---
        Row(
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = accentBlue,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RECENTLY COMPLETED",
                color = secondaryText,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }

        // --- RESULTS LIST ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(completedReports) { report ->
                InternalReportCard(
                    report = report,
                    isDarkMode = isDarkMode,
                    accentBlue = accentBlue,
                    cardBg = cardBg,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun InternalReportCard(
    report: LabReportItem,
    isDarkMode: Boolean,
    accentBlue: Color,
    cardBg: Color,
    onAction: (String) -> Unit
) {
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val dividerColor = if (isDarkMode) Color.Gray.copy(alpha = 0.2f) else Color(0xFFEEEEEE)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Patient Info Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = accentBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Science,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.patientName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = primaryText
                    )
                    Text(
                        text = report.testName,
                        color = accentBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(report.date, color = Color.Gray, fontSize = 11.sp)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = dividerColor
            )

            // Findings
            Text(
                text = "Lab Findings:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = report.resultSummary,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = primaryText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Write Prescription Button
            Button(
                onClick = { onAction(report.patientId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Medication, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WRITE PRESCRIPTION",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// --- DUAL PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProviderLabReportsLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderLabReportsScreen(isDarkMode = false, onBack = {}, onAction = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProviderLabReportsDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderLabReportsScreen(isDarkMode = true, onBack = {}, onAction = {})
    }
}