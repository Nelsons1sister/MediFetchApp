package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPrescriptionScreen(
    isDarkMode: Boolean, // ✅ Added theme support
    initialPatientId: String = "",
    onBack: () -> Unit = {},
    onPrescriptionSent: () -> Unit = {}
) {
    // --- 1. THEME VARIABLES ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.Gray else Color.DarkGray
    val containerBg = if (isDarkMode) Color(0xFF121212) else Color.White

    // --- 2. FORM STATES ---
    var patientId by remember { mutableStateOf(initialPatientId) }
    var clinicalFindings by remember { mutableStateOf("") }
    var medicationList by remember { mutableStateOf("") }
    var additionalInstructions by remember { mutableStateOf("") }

    var isSending by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(initialPatientId) {
        if (initialPatientId.isNotEmpty()) {
            patientId = initialPatientId
        }
    }

    // --- 3. SUCCESS CONFIRMATION DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = accentTeal, modifier = Modifier.size(48.dp))
            },
            title = { Text("Case Finalized", fontWeight = FontWeight.Bold, color = primaryText) },
            text = {
                Text(
                    "Both the Medical Report and the Prescription have been sent to the patient. This case is now marked as Completed.",
                    color = if (isDarkMode) Color.LightGray else Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onPrescriptionSent()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
                ) {
                    Text("Return to Dashboard", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = accentTeal)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Write Prescription",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )
        }

        // --- SUB-HEADER ---
        Text(
            text = "Final Diagnosis & Rx",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = accentTeal
        )
        Text(
            text = "Interpret results and issue treatment",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- PATIENT IDENTIFICATION ---
        Text("Active Patient", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = secondaryText)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = patientId,
            onValueChange = { patientId = it },
            placeholder = { Text("Enter Patient ID") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = accentTeal) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentTeal,
                unfocusedContainerColor = containerBg,
                focusedContainerColor = containerBg,
                focusedTextColor = primaryText,
                unfocusedTextColor = primaryText
            )
        )

        if (patientId.isNotEmpty()) {
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                color = accentTeal.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Linking to Case ID: $patientId",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = accentTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- THE MEDICAL REPORT ---
        Text("1. Clinical Findings", fontWeight = FontWeight.Bold, color = primaryText, fontSize = 15.sp)
        Text("Explain the lab results clearly", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = clinicalFindings,
            onValueChange = { clinicalFindings = it },
            placeholder = { Text("E.g. Malaria detected. Begin treatment.") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = containerBg,
                focusedContainerColor = containerBg,
                focusedBorderColor = accentTeal,
                focusedTextColor = primaryText,
                unfocusedTextColor = primaryText
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- THE PRESCRIPTION ---
        Text("2. Medication & Dosage", fontWeight = FontWeight.Bold, color = primaryText, fontSize = 15.sp)
        Text("List drugs and instructions", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = medicationList,
            onValueChange = { medicationList = it },
            placeholder = { Text("1. Drug A - 1 tab 2x daily") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = containerBg,
                focusedContainerColor = containerBg,
                focusedBorderColor = accentTeal,
                focusedTextColor = primaryText,
                unfocusedTextColor = primaryText
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = additionalInstructions,
            onValueChange = { additionalInstructions = it },
            label = { Text("Additional Advice") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = containerBg,
                focusedContainerColor = containerBg,
                focusedBorderColor = accentTeal,
                focusedTextColor = primaryText,
                unfocusedTextColor = primaryText
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SUBMIT ACTION ---
        Button(
            onClick = {
                isSending = true
                scope.launch {
                    delay(1500)
                    isSending = false
                    showSuccessDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
            enabled = patientId.isNotEmpty() && !isSending
        ) {
            if (isSending) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Deliver Report & Rx", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

// --- PREVIEWS ---
@Preview(name = "Prescription Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderPrescriptionLightPreview() {
    ProviderPrescriptionScreen(isDarkMode = false, initialPatientId = "P-102938")
}

@Preview(name = "Prescription Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderPrescriptionDarkPreview() {
    ProviderPrescriptionScreen(isDarkMode = true, initialPatientId = "P-102938")
}