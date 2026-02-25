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

// --- DATA MODELS ---
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
fun ProviderClinicalScreen(
    isDarkMode: Boolean // ✅ Theme support
) {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedCase by remember { mutableStateOf<ClinicalCase?>(null) }

    // Theme Variables
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val cardContainerBg = if (isDarkMode) Color(0xFF121212) else Color.White

    val clinicalCases = remember {
        listOf(
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
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* General Upload */ },
                containerColor = accentTeal,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Clinical Management", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = primaryText)
            Text("Smart Review: Tests are verified automatically", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search Patient ID", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = accentTeal) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = cardContainerBg,
                    focusedContainerColor = cardContainerBg,
                    focusedBorderColor = accentTeal,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(clinicalCases) { medicalCase ->
                    ClinicalCaseGroup(
                        medicalCase = medicalCase,
                        isDarkMode = isDarkMode,
                        accentTeal = accentTeal,
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
                containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
            ) {
                ConsultationContent(
                    medicalCase = selectedCase!!,
                    isDarkMode = isDarkMode,
                    accentTeal = accentTeal,
                    onDone = { showSheet = false }
                )
            }
        }
    }
}

@Composable
fun ClinicalCaseGroup(
    medicalCase: ClinicalCase,
    isDarkMode: Boolean,
    accentTeal: Color,
    onConsultClick: () -> Unit
) {
    Column {
        ClinicalItemCard(
            title = medicalCase.mainIllness,
            patient = medicalCase.patientName,
            status = medicalCase.status,
            type = ItemType.PATIENT_ILLNESS_REPORT,
            isDarkMode = isDarkMode,
            accentTeal = accentTeal,
            onAction = onConsultClick
        )

        if (medicalCase.investigations.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp, bottom = 8.dp)) {
                Text("TEST REPORTS FOUND", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
                medicalCase.investigations.forEach { test ->
                    Spacer(modifier = Modifier.height(8.dp))
                    ClinicalItemCard(
                        test.testName,
                        medicalCase.patientName,
                        test.status,
                        ItemType.LAB_RESULT,
                        isSmall = true,
                        isDarkMode = isDarkMode,
                        accentTeal = accentTeal
                    )
                }
            }
        }

        if (medicalCase.prescription != null) {
            Spacer(modifier = Modifier.height(8.dp))
            ClinicalItemCard(
                medicalCase.prescription,
                medicalCase.patientName,
                ActionStatus.SENT,
                ItemType.PRESCRIPTION,
                isDarkMode = isDarkMode,
                accentTeal = accentTeal
            )
        }
    }
}

@Composable
fun ClinicalItemCard(
    title: String,
    patient: String,
    status: ActionStatus,
    type: ItemType,
    isDarkMode: Boolean,
    accentTeal: Color,
    isSmall: Boolean = false,
    onAction: (() -> Unit)? = null
) {
    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (isDarkMode) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333333)) else null
    ) {
        Row(modifier = Modifier.padding(if (isSmall) 12.dp else 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(if (isSmall) 36.dp else 48.dp)
                    .background(
                        when (type) {
                            ItemType.PATIENT_ILLNESS_REPORT -> if (isDarkMode) Color(0xFF3E2723) else Color(0xFFFFF3E0)
                            ItemType.LAB_RESULT -> if (isDarkMode) Color(0xFF0D47A1) else Color(0xFFE3F2FD)
                            ItemType.PRESCRIPTION -> if (isDarkMode) Color(0xFF004D40) else Color(0xFFE8F5E9)
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
                        ItemType.PATIENT_ILLNESS_REPORT -> if (isDarkMode) Color(0xFFFFB74D) else Color(0xFFF57C00)
                        ItemType.LAB_RESULT -> if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF1976D2)
                        ItemType.PRESCRIPTION -> accentTeal
                    }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = if (isSmall) 14.sp else 16.sp, color = primaryText)
                Text("Patient: $patient", color = Color.Gray, fontSize = 12.sp)
            }

            if (status == ActionStatus.RECEIVED && !isSmall) {
                Button(
                    onClick = { onAction?.invoke() },
                    colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Consult", fontSize = 11.sp, color = Color.White)
                }
            } else if (status == ActionStatus.SENT) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = accentTeal, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ConsultationContent(medicalCase: ClinicalCase, isDarkMode: Boolean, accentTeal: Color, onDone: () -> Unit) {
    val hasTestResult = medicalCase.investigations.any { it.status == ActionStatus.SENT }
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val containerBg = if (isDarkMode) Color(0xFF252525) else Color.White

    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Text(
            text = if (hasTestResult) "Verified Prescription" else "Clinical Inquiry",
            fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryText
        )
        Text("Case: ${medicalCase.mainIllness}", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        if (hasTestResult) {
            Text("✅ Lab Evidence Found. You can now prescribe.", color = accentTeal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Dosage instructions for ${medicalCase.patientName}...", color = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = containerBg,
                    focusedContainerColor = containerBg,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText
                )
            )
        } else {
            Text("❌ No Test Result Found.", color = Color(0xFFE57373), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Please request a lab test before providing drugs.", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Request Lab Test (RDT/CBC)", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (hasTestResult) "Submit Prescription" else "Return to Dashboard", color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// --- PREVIEWS ---

@Preview(name = "Clinical Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderClinicalLightPreview() {
    ProviderClinicalScreen(isDarkMode = false)
}

@Preview(name = "Clinical Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderClinicalDarkPreview() {
    ProviderClinicalScreen(isDarkMode = true)
}