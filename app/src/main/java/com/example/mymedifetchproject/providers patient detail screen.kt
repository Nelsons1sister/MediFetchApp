package com.example.mymedifetchproject.provider

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ProviderPatientDetailScreen
 * Logic: Doctor reviews sickness report and triggers the lab request.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPatientDetailScreen(
    patientId: String = "MF-2026-001",
    onBack: () -> Unit,
    onOrderLab: () -> Unit
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- 1. SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2C7B76),
                    modifier = Modifier.size(40.dp)
                )
            },
            title = { Text("Request Processed", fontWeight = FontWeight.Bold) },
            text = {
                Text("The lab test request for #$patientId has been logged. The patient can now select a lab from their dashboard.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onOrderLab() // Logic: Triggers the return to Dashboard
                }) {
                    Text("OK", color = Color(0xFF2C7B76), fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Medical Case Review", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FBFB))
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // --- 2. PATIENT INFO SECTION ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE0EDED),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF2C7B76),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text("David John", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Text("ID: $patientId", color = Color.Gray, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(
                            color = Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "URGENT",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Color(0xFFE65100),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // --- 3. SYMPTOMS SECTION ---
            Text("Patient's Statement", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Text(
                    text = "High fever starting at midnight. Terrible body aches and sensitivity to light. I feel extremely dehydrated despite drinking water.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- 4. ACTION BUTTONS ---
            Text("Provider Actions", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(15.dp))

            // ✅ PRIMARY ACTION: REQUEST LAB TEST
            Button(
                onClick = { showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
            ) {
                Icon(Icons.Default.Science, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Request Lab Test", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- 5. LOGIC EXPLANATION FOOTER ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Requesting a lab test moves this case to 'Pending Lab' status.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- PREVIEW SECTION ---
@Preview(showBackground = true, name = "Patient Detail View")
@Composable
fun ProviderPatientDetailPreview() {
    ProviderPatientDetailScreen(
        patientId = "MF-2026-001",
        onBack = {},
        onOrderLab = {}
    )
}