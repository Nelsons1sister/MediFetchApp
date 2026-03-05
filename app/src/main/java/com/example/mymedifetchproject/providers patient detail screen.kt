package com.example.mymedifetchproject.provider

import androidx.compose.foundation.BorderStroke
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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPatientDetailScreen(
    patientId: String = "MF-2026-001",
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onOrderLab: () -> Unit
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- 1. DYNAMIC PALETTE (SYCHRONIZED WITH DASHBOARD) ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF) // Match Dashboard Blue-White
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1) // Match Dashboard Blue

    // --- 2. SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = accentBlue,
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
                    onOrderLab()
                }) {
                    Text("OK", color = accentBlue, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = primaryText,
                    navigationIconContentColor = primaryText
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // --- 3. PATIENT IDENTITY CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
                border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = accentBlue.copy(alpha = 0.15f),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = accentBlue,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text("David John", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = primaryText)
                            Text("ID: $patientId", color = secondaryText, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(
                            color = if (isDarkMode) Color(0xFF3E2723) else Color(0xFFFFF3E0),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "URGENT",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = if (isDarkMode) Color(0xFFFFAB91) else Color(0xFFE65100),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // --- 4. SYMPTOMS SECTION ---
            Text("Patient's Statement", fontWeight = FontWeight.Bold, color = accentBlue)
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (isDarkMode) Color(0xFF222222) else Color(0xFFE0E0E0))
            ) {
                Text(
                    text = "High fever starting at midnight. Terrible body aches and sensitivity to light. I feel extremely dehydrated despite drinking water.",
                    modifier = Modifier.padding(16.dp),
                    color = primaryText,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- 5. ACTION BUTTONS ---
            Text("Provider Actions", fontWeight = FontWeight.Bold, color = secondaryText)
            Spacer(modifier = Modifier.height(15.dp))

            // Changed Button from Green/Teal to Dashboard Blue
            Button(
                onClick = { showSuccessDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
            ) {
                Icon(Icons.Default.Science, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Request Lab Test", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- 6. FOOTER INFO ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = secondaryText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Requesting a lab test moves this case to 'Pending Lab' status.",
                    fontSize = 12.sp,
                    color = secondaryText
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- DUAL PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ProviderDetailLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderPatientDetailScreen(isDarkMode = false, onBack = {}, onOrderLab = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun ProviderDetailDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderPatientDetailScreen(isDarkMode = true, onBack = {}, onOrderLab = {})
    }
}