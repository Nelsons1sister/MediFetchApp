//package com.example.mymedifetchproject

package com.example.mymedifetchproject.provider



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DoctorTestSelectionScreen(onBack: () -> Unit, onConfirmOrder: () -> Unit) {
    // State to track selected tests
    val testOptions = listOf("Malaria (RDT)", "Malaria (Microscopy)", "Typhoid (Widal)", "Full Blood Count", "Urinalysis")
    val selectedTests = remember { mutableStateListOf<String>() }
    var additionalNotes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Lab Test", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(text = "Select Required Tests", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Choose the diagnostics needed based on the patient's symptoms.",
                color = Color(0xFF424242),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🟢 Test Selection Chips (FlowRow is great for tags)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                testOptions.forEach { test ->
                    FilterChip(
                        selected = selectedTests.contains(test),
                        onClick = {
                            if (selectedTests.contains(test)) selectedTests.remove(test)
                            else selectedTests.add(test)
                        },
                        label = { Text(test) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE0EDED),
                            selectedLabelColor = Color(0xFF2C7B76)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🟢 Additional Instructions
            Text(text = "Special Instructions (Optional)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("e.g. Please check for parasite density...") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF2C7B76))
            )

            Spacer(modifier = Modifier.weight(1f))

            // 🟢 Confirm Button
            Button(
                onClick = onConfirmOrder,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = selectedTests.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
            ) {
                Icon(Icons.Default.Assignment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm & Send to Patient", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "The patient will receive a notification to visit a lab for the selected tests.",
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorTestSelectionPreview() {
    DoctorTestSelectionScreen(onBack = {}, onConfirmOrder = {})
}