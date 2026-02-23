package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// 1. Data model for the Provider's view of a patient
data class ProviderPatient(
    val name: String,
    val id: String,
    val lastUpdate: String,
    val status: String,
    val isUrgent: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPatientListScreen(onPatientClick: (String) -> Unit) {
    // State for searching
    var searchQuery by remember { mutableStateOf("") }

    // Mock Data List
    val allPatients = remember {
        listOf(
            ProviderPatient("David John", "MF-2026-001", "2 hours ago", "Awaiting Lab", true),
            ProviderPatient("Sarah Adams", "MF-2026-042", "5 hours ago", "Result Uploaded", false),
            ProviderPatient("Michael Obi", "MF-2026-089", "Yesterday", "Completed", false),
            ProviderPatient("Blessing Okoro", "MF-2026-112", "3 days ago", "Awaiting Lab", true),
            ProviderPatient("Emmanuel Tunde", "MF-2026-205", "Just Now", "In Progress", false)
        )
    }

    // Filtered List Logic
    val filteredPatients = allPatients.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.id.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .padding(20.dp)
    ) {
        Text("Facility Patients", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Manage active cases and reviews", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Search and Filter Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search ID or Name", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { /* Could open a bottom sheet filter */ },
                modifier = Modifier
                    .size(52.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color(0xFF2C7B76))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Patient List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredPatients) { patient ->
                PatientListItem(patient, onPatientClick)
            }

            // Empty State
            if (filteredPatients.isEmpty()) {
                item {
                    Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No patients found matching '$searchQuery'", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListItem(patient: ProviderPatient, onClick: (String) -> Unit) {
    Card(
        onClick = { onClick(patient.id) }, // Navigates using the unique ID
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Patient Initials Avatar
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color(0xFFE0EDED), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val initials = patient.name.split(" ").map { it.take(1) }.joinToString("")
                Text(
                    text = initials,
                    color = Color(0xFF2C7B76),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(patient.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(patient.id, color = Color.Gray, fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                if (patient.isUrgent) {
                    Text("URGENT", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                Text(
                    text = patient.status,
                    color = Color(0xFF2C7B76),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(patient.lastUpdate, color = Color.LightGray, fontSize = 10.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProviderPatientListPreview() {
    ProviderPatientListScreen(onPatientClick = {})
}