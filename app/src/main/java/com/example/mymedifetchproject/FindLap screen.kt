package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

// Keep your existing Data Model
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
    onNavigateToCheckIn: (String, String) -> Unit // <--- ADDED THIS FOR NAVGRAPH
) {
    var searchQuery by remember { mutableStateOf("") }

    val requestedTests = listOf("Malaria (RDT)", "Typhoid (Widal)")
    val orderingDoctor = "Dr. Smith"

    val allLabs = listOf(
        LabFacility("1", "North Gate Diagnostics", "12 Airport Road", "North", "0.8 km", "+234 801...", listOf("Malaria RDT", "Blood Count")),
        LabFacility("2", "Savannah Lab Services", "45 Market Square", "North", "2.1 km", "+234 703...", listOf("Urinalysis", "Malaria RDT")),
        LabFacility("3", "Central Health Hub", "101 Main Street", "Central", "5.4 km", "+234 902...", listOf("X-Ray", "CBC")),
        LabFacility("4", "Southern Cross Meds", "88 Coast Road", "South", "12.0 km", "+234 815...", listOf("MRI", "Full Checkup"))
    )

    val filteredLabs = allLabs.filter {
        it.region.contains(searchQuery, ignoreCase = true) || it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF2C7B76))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Find a Lab", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C7B76))
                Text("Select a facility to check-in", color = Color.Gray, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // DOCTOR'S ORDER CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0EDED))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ACTIVE TEST ORDER", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = Color(0xFF2C7B76))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tests: ${requestedTests.joinToString(", ")}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Requested by: $orderingDoctor", fontSize = 13.sp, color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by area or name...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(filteredLabs) { lab ->
                LabItemCard(
                    lab = lab,
                    onCheckIn = {
                        // INSTEAD of showing a dialog, we trigger the NavGraph to go to the confirmation screen
                        onNavigateToCheckIn(lab.name, lab.address)
                    }
                )
            }
        }
    }
}

@Composable
fun LabItemCard(lab: LabFacility, onCheckIn: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(48.dp).background(Color(0xFFF1F8F7), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Science, contentDescription = null, tint = Color(0xFF2C7B76))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(lab.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${lab.address} (${lab.region})", color = Color.Gray, fontSize = 12.sp)
                }
                Text(lab.distance, color = Color(0xFF2C7B76), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(lab.phone, fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = { /* Directions Logic */ },
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Map", fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onCheckIn,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Check-in", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PatientFindLabPreview() {
    PatientFindLabScreen(
        onBack = {},
        onNavigateToCheckIn = { _, _ -> }
    )
}