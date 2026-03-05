package com.example.mymedifetchproject.provider

import androidx.compose.foundation.BorderStroke
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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

data class ProviderPatient(
    val name: String,
    val id: String,
    val lastUpdate: String,
    val status: String,
    val isUrgent: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderPatientListScreen(
    isDarkMode: Boolean,
    onPatientClick: (String) -> Unit
) {
    // --- 1. DYNAMIC PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    var searchQuery by remember { mutableStateOf("") }

    // --- LOGIC EMBEDDED: EXPANDED PATIENT LIST ---
    val allPatients = remember {
        listOf(
            ProviderPatient("David John", "MF-2026-001", "2 hours ago", "Awaiting Lab", true),
            ProviderPatient("Sarah Adams", "MF-2026-042", "5 hours ago", "Result Uploaded", false),
            ProviderPatient("Michael Obi", "MF-2026-089", "Yesterday", "Completed", false),
            ProviderPatient("Blessing Okoro", "MF-2026-112", "3 days ago", "Awaiting Lab", true),
            ProviderPatient("Emmanuel Tunde", "MF-2026-205", "Just Now", "In Progress", false),
            ProviderPatient("Kriti Sanjeev", "MF-2026-310", "1 hour ago", "Urgent Review", true),
            ProviderPatient("Samuel Okon", "MF-2026-415", "4 hours ago", "Awaiting Lab", false),
            ProviderPatient("Grace Peters", "MF-2026-520", "2 days ago", "Completed", false),
            ProviderPatient("Isabella Duke", "MF-2026-625", "3 hours ago", "Result Uploaded", false),
            ProviderPatient("Victor Amechi", "MF-2026-730", "Just Now", "In Progress", true)
        )
    }

    // --- LOGIC EMBEDDED: REAL-TIME SEARCH FILTER ---
    val filteredPatients = allPatients.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.id.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(20.dp)
    ) {
        Text("Facility Patients", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = primaryText)
        Text("Manage active cases and reviews", color = secondaryText, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Search and Filter Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search ID or Name", fontSize = 14.sp, color = secondaryText) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = accentBlue) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = cardBg,
                    focusedContainerColor = cardBg,
                    unfocusedBorderColor = if (isDarkMode) Color(0xFF222222) else Color.Transparent,
                    focusedBorderColor = accentBlue,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { /* Open Filter */ },
                modifier = Modifier
                    .size(52.dp)
                    .background(cardBg, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = accentBlue)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Patient List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredPatients) { patient ->
                PatientListItem(
                    patient = patient,
                    isDarkMode = isDarkMode,
                    cardBg = cardBg,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    accentBlue = accentBlue,
                    onClick = onPatientClick
                )
            }

            // Logic for Empty Search Results
            if (filteredPatients.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No patients found", color = secondaryText, fontWeight = FontWeight.Bold)
                            Text("Try a different name or ID", color = secondaryText, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListItem(
    patient: ProviderPatient,
    isDarkMode: Boolean,
    cardBg: Color,
    primaryText: Color,
    secondaryText: Color,
    accentBlue: Color,
    onClick: (String) -> Unit
) {
    Card(
        onClick = { onClick(patient.id) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 1.dp),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Patient Initials Avatar
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(accentBlue.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val initials = patient.name.split(" ").map { it.take(1) }.joinToString("")
                Text(
                    text = initials,
                    color = accentBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(patient.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryText)
                Text(patient.id, color = secondaryText, fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                if (patient.isUrgent) {
                    Text("URGENT", color = Color(0xFFFF5252), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
                Text(
                    text = patient.status,
                    color = accentBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(patient.lastUpdate, color = secondaryText.copy(alpha = 0.7f), fontSize = 10.sp)
            }
        }
    }
}

// --- DUAL PREVIEWS ---
@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ProviderPatientListLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderPatientListScreen(isDarkMode = false, onPatientClick = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun ProviderPatientListDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderPatientListScreen(isDarkMode = true, onPatientClick = {})
    }
}