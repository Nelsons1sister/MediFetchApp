package com.example.mymedifetchproject.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun LabCheckInScreen(
    labName: String,
    labAddress: String,
    requestedTests: String,
    onConfirm: () -> Unit, // This will be triggered after the Success Dialog
    onCancel: () -> Unit,
    isDarkMode: Boolean // Removed default value to force global sync
) {
    // --- 1. SUCCESS DIALOG STATE ---
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- 2. DYNAMIC THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color(0xFF1B1B1B)
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val iconSurface = if (isDarkMode) Color(0xFF002B28) else Color(0xFFE0F2F1)

    // --- 3. SUCCESS DIALOG LOGIC ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismiss on outside click */ },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = accentTeal,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "Check-In Successful",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "You have successfully checked in at $labName. Your status has been updated on your dashboard. Please proceed to the reception.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onConfirm() // Triggers the navigation back to Dashboard
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Arrival", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = primaryText,
                    navigationIconContentColor = accentTeal
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- 4. LOCATION DECORATION ---
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = iconSurface,
                modifier = Modifier.size(88.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = accentTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Are you at the facility?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Text(
                text = "Please confirm your arrival to notify the lab technician of your presence.",
                textAlign = TextAlign.Center,
                color = secondaryText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 5. LAB INFO CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null,
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = labName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = accentTeal,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = labAddress,
                        textAlign = TextAlign.Center,
                        color = secondaryText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        thickness = 0.5.dp,
                        color = secondaryText.copy(alpha = 0.2f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = accentTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tests to be Processed",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = primaryText
                        )
                    }

                    Text(
                        text = requestedTests,
                        color = secondaryText,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- 6. ACTION BUTTONS ---
            Button(
                onClick = { showSuccessDialog = true }, // ✅ Triggers dialog instead of immediate nav
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    "Confirm & Check-In",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            TextButton(
                onClick = onCancel,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    "Cancel",
                    color = if (isDarkMode) Color(0xFFFF8A80) else Color.Red,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// --- 7. DUAL PREVIEWS ---

@Preview(name = "Light Mode Check-In", showBackground = true, showSystemUi = true)
@Composable
fun LabCheckInPreviewLight() {
    LabCheckInScreen(
        labName = "North Gate Diagnostics",
        labAddress = "123 Medical Lane, Lagos",
        requestedTests = "Malaria RDT, Full Blood Count",
        onConfirm = {},
        onCancel = {},
        isDarkMode = false
    )
}

@Preview(name = "Dark Mode Check-In", showBackground = true, showSystemUi = true)
@Composable
fun LabCheckInPreviewDark() {
    LabCheckInScreen(
        labName = "North Gate Diagnostics",
        labAddress = "123 Medical Lane, Lagos",
        requestedTests = "Malaria RDT, Full Blood Count",
        onConfirm = {},
        onCancel = {},
        isDarkMode = true
    )
}