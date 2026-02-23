//package com.example.mymedifetchproject

package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// 1. Unified Data Models
enum class ActionStatus {
    RECEIVED, AWAITING_LAB_TEST, SENT, AWAITING_SIGNATURE
}

enum class ItemType {
    PATIENT_ILLNESS_REPORT, LAB_RESULT, PRESCRIPTION
}

data class ClinicalCase(
    val mainIllness: String,
    val patientName: String,
    val date: String,
    val status: ActionStatus,
    val investigations: List<TestReport> = emptyList(),
    val prescription: String? = null
)

data class TestReport(
    val testName: String,
    val status: ActionStatus
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderClinicalScreen() {
    // State for the Smart Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedCase by remember { mutableStateOf<ClinicalCase?>(null) }

    // Unified List: John Doe needs a test, Sarah Jenkins is ready for drugs
    val clinicalCases = listOf(
        ClinicalCase(
            mainIllness = "Fever & Shivering",
            patientName = "John Doe",
            date = "11:00 AM",
            status = ActionStatus.RECEIVED
        ),
        ClinicalCase(
            mainIllness = "Malaria Symptoms",
            patientName = "Sarah Jenkins",
            date = "Feb 18, 2026",
            status = ActionStatus.AWAITING_LAB_TEST,
            investigations = listOf(
                TestReport("Full Blood Count", ActionStatus.SENT)
            ),
            prescription = "Artemether Dosage"
        )
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* General Upload */ },
                containerColor = Color(0xFF2C7B76),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8FBFB)).padding(padding).padding(20.dp)
        ) {
            Text("Clinical Management", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Smart Review: Tests are verified automatically", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Patient ID (e.g. MF-2025-001)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(clinicalCases) { medicalCase ->
                    // Pass the click logic into the group
                    ClinicalCaseGroup(
                        medicalCase = medicalCase,
                        onConsultClick = {
                            selectedCase = medicalCase
                            showSheet = true
                        }
                    )
                }
            }
        }

        // --- THE SMART BOTTOM SHEET ---
        if (showSheet && selectedCase != null) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                ConsultationContent(
                    medicalCase = selectedCase!!,
                    onDone = { showSheet = false }
                )
            }
        }
    }
}

@Composable
fun ClinicalCaseGroup(medicalCase: ClinicalCase, onConsultClick: () -> Unit) {
    Column {
        // Parent Card
        ClinicalItemCard(
            title = medicalCase.mainIllness,
            patient = medicalCase.patientName,
            status = medicalCase.status,
            type = ItemType.PATIENT_ILLNESS_REPORT,
            onAction = onConsultClick
        )

        // Nested Investigations
        if (medicalCase.investigations.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp, bottom = 8.dp)) {
                Text("TEST REPORTS FOUND", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.LightGray)
                medicalCase.investigations.forEach { test ->
                    Spacer(modifier = Modifier.height(8.dp))
                    ClinicalItemCard(test.testName, medicalCase.patientName, test.status, ItemType.LAB_RESULT, isSmall = true)
                }
            }
        }

        // Final Prescription
        if (medicalCase.prescription != null) {
            Spacer(modifier = Modifier.height(8.dp))
            ClinicalItemCard(medicalCase.prescription, medicalCase.patientName, ActionStatus.SENT, ItemType.PRESCRIPTION)
        }
    }
}

@Composable
fun ClinicalItemCard(
    title: String,
    patient: String,
    status: ActionStatus,
    type: ItemType,
    isSmall: Boolean = false,
    onAction: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(if (isSmall) 1.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(if (isSmall) 12.dp else 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(if (isSmall) 36.dp else 48.dp).background(
                    when (type) {
                        ItemType.PATIENT_ILLNESS_REPORT -> Color(0xFFFFF3E0)
                        ItemType.LAB_RESULT -> Color(0xFFE3F2FD)
                        ItemType.PRESCRIPTION -> Color(0xFFE8F5E9)
                    }, RoundedCornerShape(8.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (type) {
                        ItemType.PATIENT_ILLNESS_REPORT -> Icons.Default.ChatBubbleOutline
                        ItemType.LAB_RESULT -> Icons.Default.Science
                        ItemType.PRESCRIPTION -> Icons.Default.Medication
                    },
                    contentDescription = null,
                    modifier = Modifier.size(if (isSmall) 20.dp else 24.dp),
                    tint = when (type) {
                        ItemType.PATIENT_ILLNESS_REPORT -> Color(0xFFF57C00)
                        ItemType.LAB_RESULT -> Color(0xFF1976D2)
                        ItemType.PRESCRIPTION -> Color(0xFF2C7B76)
                    }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = if (isSmall) 14.sp else 16.sp)
                Text("Patient: $patient", color = Color.Gray, fontSize = 12.sp)
            }

            if (status == ActionStatus.RECEIVED && !isSmall) {
                Button(
                    onClick = { onAction?.invoke() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Consult", fontSize = 11.sp)
                }
            } else if (status == ActionStatus.SENT) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ConsultationContent(medicalCase: ClinicalCase, onDone: () -> Unit) {
    // THE SMART CHECK: We look into the nested investigations list
    val hasTestResult = medicalCase.investigations.any { it.status == ActionStatus.SENT }

    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Text(
            text = if (hasTestResult) "Verified Prescription" else "Clinical Inquiry",
            fontSize = 20.sp, fontWeight = FontWeight.Bold
        )
        Text("Case: ${medicalCase.mainIllness}", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        if (hasTestResult) {
            Text("✅ Lab Evidence Found. You can now prescribe.", color = Color(0xFF2C7B76), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Dosage instructions for ${medicalCase.patientName}...") },
                shape = RoundedCornerShape(12.dp)
            )
        } else {
            Text("❌ No Test Result Found.", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Please request a lab test before providing drugs.", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Request Lab Test (RDT/CBC)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (hasTestResult) "Submit Prescription" else "Return to Dashboard")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProviderClinicalScreenPreview() {
    ProviderClinicalScreen()
}