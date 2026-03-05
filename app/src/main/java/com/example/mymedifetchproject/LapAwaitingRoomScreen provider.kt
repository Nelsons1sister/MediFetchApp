package com.example.mymedifetchproject.provider

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Science
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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabWaitingRoomScreen(
    patientName: String = "David John",
    labUnit: String = "Diagnostics Unit 1",
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onConfirmCheckIn: () -> Unit
) {
    // --- Dynamic Theme Palette (Synced with Dashboard Blue) ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Patient Check-In", fontWeight = FontWeight.Bold, color = primaryText)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = accentBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // --- Visual Status Indicator ---
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = accentBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(140.dp)
                ) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = null,
                        tint = accentBlue,
                        modifier = Modifier.padding(36.dp)
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = bgColor,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Status Active",
                        tint = Color(0xFF4CAF50), // Standard Success Green
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Patient Information ---
            Text(
                text = "PATIENT QUEUED",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = accentBlue,
                letterSpacing = 1.2.sp
            )

            Text(
                text = patientName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = primaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Assigned to: $labUnit",
                color = secondaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Instruction Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = accentBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Next Lab Steps",
                            fontWeight = FontWeight.Bold,
                            color = primaryText,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "• Verify the patient's ID band.\n" +
                                "• Collect blood sample for Malaria RDT.\n" +
                                "• Update results in the digital portal.",
                        fontSize = 15.sp,
                        color = if (isDarkMode) Color.LightGray else Color.DarkGray,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Action Button ---
            Button(
                onClick = onConfirmCheckIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue)
            ) {
                Text(
                    "START PROCESSING",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun PreviewWaitingRoomLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        LabWaitingRoomScreen(
            isDarkMode = false,
            onBack = {},
            onConfirmCheckIn = {}
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun PreviewWaitingRoomDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        LabWaitingRoomScreen(
            isDarkMode = true,
            onBack = {},
            onConfirmCheckIn = {}
        )
    }
}