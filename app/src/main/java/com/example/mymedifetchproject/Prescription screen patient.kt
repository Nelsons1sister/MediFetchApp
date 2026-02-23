package com.example.mymedifetchproject.patient



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientPrescriptionScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Prescriptions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
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
            Spacer(modifier = Modifier.height(20.dp))

            // --- Header Section ---
            Text(
                text = "Active Medications",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2C7B76)
            )
            Text(
                text = "Please follow the dosage instructions carefully.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Prescription List (Mock Data) ---
            PrescriptionItem(
                medName = "Amoxicillin",
                dosage = "500mg - 1 Tablet",
                frequency = "Twice daily (Morning & Night)",
                duration = "7 Days",
                instructions = "Take after meals. Complete the full course even if you feel better."
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrescriptionItem(
                medName = "Paracetamol",
                dosage = "1000mg - 2 Tablets",
                frequency = "Every 6 hours",
                duration = "3 Days",
                instructions = "Only take if fever exceeds 38°C. Do not exceed 8 tablets in 24 hours."
            )

            Spacer(modifier = Modifier.height(30.dp))

            // --- Help Section ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0EDED)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2C7B76))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "If you experience any allergic reactions, stop medication and contact your provider immediately.",
                        fontSize = 12.sp,
                        color = Color(0xFF2C7B76),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PrescriptionItem(
    medName: String,
    dosage: String,
    frequency: String,
    duration: String,
    instructions: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F8F8),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Medication,
                        contentDescription = null,
                        tint = Color(0xFF2C7B76),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(medName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(dosage, fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Badge(containerColor = Color(0xFFE0F2F1), contentColor = Color(0xFF00796B)) {
                    Text(duration, modifier = Modifier.padding(4.dp))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)

            Row {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = frequency, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = instructions,
                fontSize = 12.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientPrescriptionPreview() {
    PatientPrescriptionScreen(onBack = {})
}

