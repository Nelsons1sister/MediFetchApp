package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPrescriptionScreen(
    initialPatientId: String = "",
    onBack: () -> Unit = {}, // Added for navigation
    onPrescriptionSent: () -> Unit = {}
) {
    // --- 1. FORM STATES ---
    var patientId by remember { mutableStateOf(initialPatientId) }
    var clinicalFindings by remember { mutableStateOf("") }
    var medicationList by remember { mutableStateOf("") }
    var additionalInstructions by remember { mutableStateOf("") }

    // UI Feedback States
    var isSending by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Sync state if initialPatientId changes via NavGraph
    LaunchedEffect(initialPatientId) {
        if (initialPatientId.isNotEmpty()) {
            patientId = initialPatientId
        }
    }

    // --- 2. SUCCESS CONFIRMATION DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(48.dp))
            },
            title = { Text("Case Finalized", fontWeight = FontWeight.Bold) },
            text = {
                Text("Both the Medical Report and the Prescription have been sent to the patient. This case is now marked as Completed.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onPrescriptionSent()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
                ) {
                    Text("Return to Dashboard")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write Prescription", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Final Diagnosis & Rx",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C7B76)
            )
            Text(
                text = "Interpret results and issue treatment",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. PATIENT IDENTIFICATION ---
            Text("Active Patient", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = patientId,
                onValueChange = { patientId = it },
                placeholder = { Text("Patient ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF2C7B76)
                )
            )

            if (patientId.isNotEmpty()) {
                Surface(
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFFE0EDED),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Linking to Case ID: $patientId",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color(0xFF2C7B76),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- 4. THE MEDICAL REPORT ---
            Text("1. Clinical Findings / Medical Report", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text("Explain what the lab results mean", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = clinicalFindings,
                onValueChange = { clinicalFindings = it },
                placeholder = { Text("E.g. Lab results confirm Malaria ++. Patient should begin treatment immediately.") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 5. THE PRESCRIPTION ---
            Text("2. Medication & Dosage", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Text("List drugs and how they should be taken", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = medicationList,
                onValueChange = { medicationList = it },
                placeholder = { Text("1. Lonart - 1 tab 12hrly for 3 days\n2. Paracetamol - 2 tabs 8hrly") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = additionalInstructions,
                onValueChange = { additionalInstructions = it },
                label = { Text("Additional Advice (Rest, Diet, etc.)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 6. SUBMIT ACTION ---
            Button(
                onClick = {
                    isSending = true
                    scope.launch {
                        delay(1500)
                        isSending = false
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                enabled = patientId.isNotEmpty() && !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.FileUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Deliver Report & Rx", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProviderPrescriptionPreview() {
    // Simulating a patient ID coming from the Lab Reports screen
    ProviderPrescriptionScreen(initialPatientId = "P-102938")
}