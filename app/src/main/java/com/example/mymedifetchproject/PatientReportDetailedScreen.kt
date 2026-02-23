package com.example.mymedifetchproject.patient



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientReportDetailScreen(
    reportId: String,
    onBack: () -> Unit
) {
    // Mock data based on your malaria logic
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle Download/Print */ }) {
                        Icon(Icons.Default.Download, contentDescription = "Download")
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER STATUS ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), // Light Red for positive result
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFC62828))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Diagnosis: Positive", fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        Text("Please follow the prescription below.", fontSize = 12.sp, color = Color(0xFFC62828))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LAB DETAILS ---
            Text("Laboratory Information", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow(label = "Case ID", value = "#$reportId")
                    DetailRow(label = "Lab Facility", value = labName)
                    DetailRow(label = "Date Released", value = date)
                    DetailRow(label = "Test Performed", value = testType)

                    Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

                    Text("Result Details:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = diagnosis,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2C7B76),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PRESCRIPTION SECTION ---
            Text("Doctor's Prescription", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = Color(0xFF2C7B76))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(doctorName, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = prescription,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Note: Complete the full dosage even if symptoms disappear.",
                        fontSize = 11.sp,
                        color = Color(0xFF2C7B76),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- ACTION BUTTON ---
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Return to Reports")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PatientReportDetailPreview() {
    PatientReportDetailScreen(
        reportId = "MF-2026-001",
        onBack = {}
    )
}