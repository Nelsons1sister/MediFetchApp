package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSicknessScreen(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    // --- 1. THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF424242)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val fieldBg = if (isDarkMode) Color(0xFF1A1A1A) else Color(0xFFF9F9F9)
    val dialogBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    // --- 2. STATE MANAGEMENT ---
    var symptomText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // --- 3. SUCCESS DIALOG LOGIC ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismissal by tapping outside */ },
            containerColor = dialogBg,
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = accentTeal,
                    modifier = Modifier.size(64.dp)
                )
            },
            title = {
                Text(
                    "Report Sent!",
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "Your provider has received your report. Please keep an eye on your notifications for next steps.",
                    color = secondaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSubmitted() // Navigation happens here
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Back to Dashboard", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Sickness", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isSending) {
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
                .padding(20.dp)
        ) {
            Text(
                text = "Describe how you feel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )
            Text(
                text = "Be as specific as possible about your symptoms and when they started.",
                color = secondaryText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- TEXT AREA ---
            OutlinedTextField(
                value = symptomText,
                onValueChange = { symptomText = it },
                enabled = !isSending,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = {
                    Text(
                        "I have been feeling cold and shivering since last night...",
                        color = secondaryText.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = if (isDarkMode) Color(0xFF333333) else Color.LightGray,
                    unfocusedContainerColor = fieldBg,
                    focusedContainerColor = fieldBg,
                    unfocusedTextColor = primaryText,
                    focusedTextColor = primaryText
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- SEND BUTTON ---
            Button(
                onClick = {
                    if (symptomText.isNotEmpty()) {
                        isSending = true
                        scope.launch {
                            delay(2000) // Simulating network send
                            isSending = false
                            showSuccessDialog = true // Trigger the dialog
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = symptomText.isNotEmpty() && !isSending,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentTeal,
                    disabledContainerColor = accentTeal.copy(alpha = 0.4f)
                )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send to Provider", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Note: In case of emergency, please visit the nearest hospital immediately.",
                fontSize = 12.sp,
                color = secondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// --- PREVIEWS ---

@Preview(name = "Light Mode")
@Composable
fun PreviewReportLight() {
    ReportSicknessScreen(onBack = {}, onSubmitted = {}, isDarkMode = false)
}

@Preview(name = "Dark Mode")
@Composable
fun PreviewReportDark() {
    ReportSicknessScreen(onBack = {}, onSubmitted = {}, isDarkMode = true)
}