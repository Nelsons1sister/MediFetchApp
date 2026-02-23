//package com.example.mymedifetchproject.provider
//
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//// Data model for the patient queue
//data class ArrivedPatient(
//    val caseId: String,
//    val patientName: String,
//    val testRequired: String,
//    val arrivalTime: String
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LabWaitingRoomScreen(
//    onBack: () -> Unit,
//    onStartTest: (String) -> Unit // This connects to your NavGraph logic
//) {
//    // Mock Data: Next week this comes from your database
//    val arrivedPatients = listOf(
//        ArrivedPatient("MF-2026-001", "David John", "Malaria RDT", "10:15 AM"),
//        ArrivedPatient("MF-2026-045", "Sarah Adams", "Typhoid Test", "10:30 AM")
//    )
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Lab Waiting Room", fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF8FBFB))
//                .padding(padding)
//                .padding(horizontal = 20.dp)
//        ) {
//            Text(
//                "Patients Ready for Sampling",
//                color = Color.Gray,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(vertical = 12.dp)
//            )
//
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                items(arrivedPatients) { patient ->
//                    WaitingPatientCard(patient, onStartTest)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WaitingPatientCard(patient: ArrivedPatient, onAction: (String) -> Unit) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(1.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Patient Avatar
//            Surface(shape = CircleShape, color = Color(0xFFF1F8F7), modifier = Modifier.size(40.dp)) {
//                Box(contentAlignment = Alignment.Center) {
//                    Text(patient.patientName.take(1), color = Color(0xFF2C7B76), fontWeight = FontWeight.Bold)
//                }
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(patient.patientName, fontWeight = FontWeight.Bold)
//                Text(patient.testRequired, color = Color(0xFF2C7B76), fontSize = 12.sp)
//                Text("ID: ${patient.caseId}", color = Color.Gray, fontSize = 11.sp)
//            }
//
//            // The button that triggers the jump to UploadResultScreen
//            Button(
//                onClick = { onAction(patient.caseId) },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text("Collect", fontSize = 12.sp)
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LabWaitingRoomPreview() {
//    LabWaitingRoomScreen(onBack = {}, onStartTest = {})
//}