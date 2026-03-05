package com.example.mymedifetchproject.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

// --- 1. DATA MODEL ---
data class LabFacility(
    val id: String,
    val name: String,
    val address: String,
    val region: String,
    val distance: String,
    val phone: String,
    val services: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientFindLabScreen(
    onBack: () -> Unit,
    onNavigateToCheckIn: (String, String) -> Unit,
    isDarkMode: Boolean
) {
    // --- 2. DYNAMIC THEME PALETTE ---
    // ✅ UPDATED: Matching the soft-blue experience (0xFFF5F9FF)
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray

    // ✅ UPDATED: Matching the branding Blue-Teal (Deep Blue)
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val orderCardBg = if (isDarkMode) Color(0xFF0D1B2A) else Color(0xFFE3F2FD)

    var searchQuery by remember { mutableStateOf("") }

    // --- 3. MOCK DATA ---
    val requestedTests = listOf("Malaria (RDT)", "Typhoid (Widal)")
    val orderingDoctor = "Dr. Smith"

    val allLabs = remember {
        listOf(
            LabFacility("1", "North Gate Diagnostics", "12 Airport Road", "North", "0.8 km", "+234 801 000 0000", listOf("Malaria RDT")),
            LabFacility("2", "Savannah Lab Services", "45 Market Square", "North", "2.1 km", "+234 703 000 0000", listOf("Malaria RDT")),
            LabFacility("3", "Central Health Hub", "101 Main Street", "Central", "5.4 km", "+234 902 000 0000", listOf("CBC")),
            LabFacility("4", "Southern Cross Meds", "88 Coast Road", "South", "12.0 km", "+234 815 000 0000", listOf("MRI"))
        )
    }

    val filteredLabs = allLabs.filter {
        it.region.contains(searchQuery, ignoreCase = true) ||
                it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // --- 4. HEADER ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(cardBg, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = accentBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Find a Lab", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primaryText)
                Text("Select a facility to check-in", color = secondaryText, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. DOCTOR'S ORDER CARD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = orderCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = accentBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ACTIVE TEST ORDER", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = accentBlue)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tests: ${requestedTests.joinToString(", ")}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryText)
                Text("Requested by: $orderingDoctor", fontSize = 13.sp, color = if (isDarkMode) Color.LightGray else Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 6. SEARCH BAR ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by area or name...", fontSize = 14.sp, color = secondaryText) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = secondaryText) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = cardBg,
                focusedContainerColor = cardBg,
                unfocusedTextColor = primaryText,
                focusedTextColor = primaryText,
                cursorColor = accentBlue,
                focusedBorderColor = accentBlue,
                unfocusedBorderColor = if (isDarkMode) Color(0xFF333333) else Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- 7. LAB LIST ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(filteredLabs) { lab ->
                LabItemCard(
                    lab = lab,
                    isDarkMode = isDarkMode,
                    cardBg = cardBg,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    accentBlue = accentBlue,
                    onCheckIn = { onNavigateToCheckIn(lab.name, lab.address) }
                )
            }
        }
    }
}

@Composable
fun LabItemCard(
    lab: LabFacility,
    isDarkMode: Boolean,
    cardBg: Color,
    primaryText: Color,
    secondaryText: Color,
    accentBlue: Color,
    onCheckIn: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = if (isDarkMode) BorderStroke(0.5.dp, Color.Gray.copy(alpha = 0.3f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(accentBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Science, contentDescription = null, tint = accentBlue)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(lab.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryText)
                    Text("${lab.address} (${lab.region})", color = secondaryText, fontSize = 12.sp)
                }
                Text(lab.distance, color = accentBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = secondaryText)
                Spacer(modifier = Modifier.width(4.dp))
                Text(lab.phone, fontSize = 12.sp, color = secondaryText)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onCheckIn,
                    colors = ButtonDefaults.buttonColors(containerColor = accentBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("Check-in", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// --- 8. PREVIEWS ---
@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewFindLabLight() {
    PatientFindLabScreen(
        onBack = {},
        onNavigateToCheckIn = { _, _ -> },
        isDarkMode = false
    )
}

@Preview(name = "OLED Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewFindLabDark() {
    PatientFindLabScreen(
        onBack = {},
        onNavigateToCheckIn = { _, _ -> },
        isDarkMode = true
    )
}