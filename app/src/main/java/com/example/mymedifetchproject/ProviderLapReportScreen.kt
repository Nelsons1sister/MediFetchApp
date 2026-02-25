package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LabReportItem(
    val patientId: String,
    val patientName: String,
    val testName: String,
    val resultSummary: String,
    val date: String
)

@Composable
fun ProviderLabReportsScreen(
    isDarkMode: Boolean, // Added for theme support
    onBack: () -> Unit,
    onNavigateToPrescribe: (String) -> Unit
) {
    // --- Dynamic Theme Palette ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.Gray else Color.DarkGray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White

    val completedReports = listOf(
        LabReportItem("P-001", "David John", "Malaria RDT", "Parasite Detected: ++", "Today, 11:30 AM"),
        LabReportItem("P-045", "Sarah Adams", "Full Blood Count", "Normal Range", "Yesterday"),
        LabReportItem("P-089", "Michael Obi", "Typhoid Widal", "Reactive 1:160", "2 days ago")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(top = 40.dp)
            .padding(horizontal = 20.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = accentTeal,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Lab Results Inbox",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )
        }

        // --- Recently Completed Tests Label ---
        Row(
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = accentTeal,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recently Completed Tests",
                color = secondaryText,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        // --- The List ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(completedReports) { report ->
                ReportCard(
                    report = report,
                    isDarkMode = isDarkMode,
                    accentTeal = accentTeal,
                    cardBg = cardBg,
                    onAction = onNavigateToPrescribe
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    report: LabReportItem,
    isDarkMode: Boolean,
    accentTeal: Color,
    cardBg: Color,
    onAction: (String) -> Unit
) {
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val dividerColor = if (isDarkMode) Color(0xFF222222) else Color(0xFFEEEEEE)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = accentTeal.copy(alpha = 0.1f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Assignment,
                            contentDescription = null,
                            tint = accentTeal,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(report.patientName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = primaryText)
                    Text(
                        report.testName,
                        color = accentTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(report.date, color = Color.Gray, fontSize = 11.sp)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = dividerColor
            )

            Text("Lab Findings:", fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color.Gray)
            Text(
                report.resultSummary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = primaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { onAction(report.patientId) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Medication, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Write Prescription", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProviderLabReportsLight() {
    ProviderLabReportsScreen(isDarkMode = false, onBack = {}, onNavigateToPrescribe = {})
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProviderLabReportsDark() {
    ProviderLabReportsScreen(isDarkMode = true, onBack = {}, onNavigateToPrescribe = {})
}