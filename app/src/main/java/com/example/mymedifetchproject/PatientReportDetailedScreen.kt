package com.example.mymedifetchproject.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientReportDetailScreen(
    reportId: String,
    onBack: () -> Unit,
    isDarkMode: Boolean
) {
    // --- 1. DYNAMIC THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    // Status Colors (Alert for Positive Result)
    val alertBg = if (isDarkMode) Color(0xFF310B0B) else Color(0xFFFFEBEE)
    val alertText = if (isDarkMode) Color(0xFFFF8A80) else Color(0xFFC62828)

    // Prescription Box Colors
    val prescrBg = if (isDarkMode) Color(0xFF0D1B2A) else Color(0xFFE3F2FD)

    // Mock data
    val diagnosis = "Plasmodium Falciparum (++)"
    val testType = "Malaria Parasite (RDT)"
    val date = "Feb 23, 2026"
    val doctorName = "Dr. Sarah Smith"
    val labName = "North Gate Diagnostics"
    val prescription = "Tab. Coartem (80/480mg) - Take 1 tablet twice daily for 3 days. Paracetamol 500mg for fever."

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Medical Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Download PDF Logic */ }) {
                        Icon(Icons.Default.Download, contentDescription = "Download")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = primaryText,
                    navigationIconContentColor = accentBlue,
                    actionIconContentColor = accentBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- 2. HEADER STATUS (Diagnosis Result) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = alertBg),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = alertText)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Diagnosis: Positive", fontWeight = FontWeight.Bold, color = alertText)
                        Text("Please follow the prescription below.", fontSize = 12.sp, color = alertText.copy(alpha = 0.8f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. LAB DETAILS ---
            Text("Laboratory Information", fontWeight = FontWeight.Bold, color = secondaryText, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null,
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow(label = "Case ID", value = "#$reportId", primaryText, secondaryText)
                    DetailRow(label = "Lab Facility", value = labName, primaryText, secondaryText)
                    DetailRow(label = "Date Released", value = date, primaryText, secondaryText)
                    DetailRow(label = "Test Performed", value = testType, primaryText, secondaryText)

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = secondaryText.copy(alpha = 0.3f)
                    )

                    Text("Result Details:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = primaryText)
                    Text(
                        text = diagnosis,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentBlue,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 4. PRESCRIPTION SECTION ---
            Text("Doctor's Prescription", fontWeight = FontWeight.Bold, color = secondaryText, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = prescrBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = accentBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(doctorName, fontWeight = FontWeight.Bold, color = if (isDarkMode) Color.White else Color.Black)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = prescription,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = if (isDarkMode) Color.White.copy(alpha = 0.9f) else Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Note: Complete the full dosage even if symptoms disappear.",
                        fontSize = 11.sp,
                        color = accentBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 5. RETURN BUTTON ---
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentBlue
                )
            ) {
                Text("Return to Reports", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, primaryText: Color, secondaryText: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = secondaryText, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = primaryText)
    }
}

// --- 6. DUAL PREVIEWS ---

@Preview(name = "Light Mode Detail", showBackground = true, showSystemUi = true)
@Composable
fun ReportDetailPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        PatientReportDetailScreen(reportId = "MF-2026-001", onBack = {}, isDarkMode = false)
    }
}

@Preview(name = "Dark Mode Detail", showBackground = true, showSystemUi = true)
@Composable
fun ReportDetailPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        PatientReportDetailScreen(reportId = "MF-2026-001", onBack = {}, isDarkMode = true)
    }
}