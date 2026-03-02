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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabWaitingRoomScreen( // Changed from LabCheckInScreen
    patientName: String = "David John",
    labUnit: String = "Diagnostics Unit 1",
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onConfirmCheckIn: () -> Unit
) {
    // --- Dynamic Theme Palette ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
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
                            tint = accentTeal
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
                    color = accentTeal.copy(alpha = 0.1f),
                    modifier = Modifier.size(140.dp)
                ) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = null,
                        tint = accentTeal,
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
                        tint = Color(0xFF43A047),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Patient Information ---
            Text(
                text = "Patient Checked-In",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentTeal,
                letterSpacing = 1.sp
            )

            Text(
                text = patientName,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Station: $labUnit",
                color = secondaryText,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Instruction Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Next Steps",
                        fontWeight = FontWeight.Bold,
                        color = primaryText,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "1. Verify the patient's ID band.\n" +
                                "2. Collect blood sample for Malaria RDT.\n" +
                                "3. Update results in the digital portal.",
                        fontSize = 14.sp,
                        color = if (isDarkMode) Color.LightGray else Color.DarkGray,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- Action Button ---
            Button(
                onClick = onConfirmCheckIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Text(
                    "Confirm & Start Lab Processing",
                    fontWeight = FontWeight.Bold,
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
fun PreviewWaitingRoomLight() { // Renamed for consistency
    LabWaitingRoomScreen(
        isDarkMode = false,
        onBack = {},
        onConfirmCheckIn = {}
    )
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun PreviewWaitingRoomDark() { // Renamed for consistency
    LabWaitingRoomScreen(
        isDarkMode = true,
        onBack = {},
        onConfirmCheckIn = {}
    )
}