//package com.example.mymedifetchproject

package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

// 1. DATA MODEL FOR DOCTOR'S INBOX
data class LabReportItem(
    val patientId: String,
    val patientName: String,
    val testName: String,
    val resultSummary: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderLabReportsScreen(
    onBack: () -> Unit,
    onNavigateToPrescribe: (String) -> Unit // This MUST match the parameter name in your NavGraph
) {
    // 2. MOCK DATA (Next week: val reports by viewModel.getCompletedLabs())
    val completedReports = listOf(
        LabReportItem("P-001", "David John", "Malaria RDT", "Parasite Detected: ++", "Today, 11:30 AM"),
        LabReportItem("P-045", "Sarah Adams", "Full Blood Count", "Normal Range", "Yesterday"),
        LabReportItem("P-089", "Michael Obi", "Typhoid Widal", "Reactive 1:160", "2 days ago")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lab Results Inbox", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FBFB))
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF2C7B76))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recently Completed Tests", color = Color.Gray, fontWeight = FontWeight.Medium)
            }

            // 3. THE LIST OF COMPLETED LABS
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(completedReports) { report ->
                    ReportCard(report, onNavigateToPrescribe)
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: LabReportItem, onAction: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color(0xFFF1F8F7), modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(report.patientName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(report.testName, color = Color(0xFF2C7B76), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Text(report.date, color = Color.Gray, fontSize = 11.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))

            Text("Lab Findings:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)
            Text(report.resultSummary, fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            // 4. THE PRESCRIBE BUTTON
            Button(
                onClick = { onAction(report.patientId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Medication, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Write Prescription")
            }
        }
    }
}

// --- 5. PREVIEW SECTION ---

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProviderLabReports() {
    // We pass empty lambdas {} for the navigation actions in the preview
    ProviderLabReportsScreen(
        onBack = { /* No-op for preview */ },
        onNavigateToPrescribe = { patientId ->
            println("Navigating to prescribe for: $patientId")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReportCardPreview() {
    // This allows you to see how an individual report card looks
    val mockReport = LabReportItem(
        patientId = "P-999",
        patientName = "John Doe",
        testName = "Blood Glucose Test",
        resultSummary = "110 mg/dL (Fasting)",
        date = "Oct 24, 2026"
    )

    Box(modifier = Modifier.padding(16.dp)) {
        ReportCard(
            report = mockReport,
            onAction = { }
        )
    }
}