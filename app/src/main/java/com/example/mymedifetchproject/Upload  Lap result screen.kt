//package com.example.mymedifetchproject.provider
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.CloudUpload
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LabUploadResultScreen(
//    caseId: String, // Dynamic Case ID from NavGraph
//    onBack: () -> Unit,
//    onUploadComplete: () -> Unit
//) {
//    var resultNotes by remember { mutableStateOf("") }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    // --- SUCCESS DIALOG ---
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = { /* Logic handled by Done button */ },
//            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(48.dp)) },
//            title = { Text("Result Sent", fontWeight = FontWeight.Bold) },
//            text = { Text("The lab result for Case #$caseId has been successfully sent. The patient and doctor can now view the findings.") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showSuccessDialog = false
//                        onUploadComplete() // 🚀 Triggers navController.popBackStack() in NavGraph
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
//                ) {
//                    Text("Done")
//                }
//            },
//            shape = RoundedCornerShape(16.dp),
//            containerColor = Color.White
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Upload Lab Result", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF8FBFB))
//                .padding(innerPadding)
//                .padding(horizontal = 24.dp)
//        ) {
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Case Tracking Header
//            Surface(
//                color = Color(0xFFE0EDED),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text(
//                    text = "ACTIVE CASE: #$caseId",
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                    color = Color(0xFF2C7B76),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 12.sp
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = "Patient: David John", fontWeight = FontWeight.Bold, fontSize = 20.sp)
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            // --- 1. PHOTO UPLOAD SECTION ---
//            Text("Evidence/Attachment", fontWeight = FontWeight.Bold, color = Color.DarkGray)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Card(
//                onClick = { /* Implement Image Picker */ },
//                modifier = Modifier.fillMaxWidth().height(160.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                border = CardDefaults.outlinedCardBorder()
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFF2C7B76))
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Attach Result Image", fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                    Text("Upload scan or photo of the lab slip", color = Color.Gray, fontSize = 12.sp)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // --- 2. NOTES SECTION ---
//            Text("Findings & Values", fontWeight = FontWeight.Bold, color = Color.DarkGray)
//            Spacer(modifier = Modifier.height(8.dp))
//            OutlinedTextField(
//                value = resultNotes,
//                onValueChange = { resultNotes = it },
//                modifier = Modifier.fillMaxWidth().height(140.dp),
//                placeholder = { Text("e.g. PCV: 35%, Malaria Parasite: ++ detected") },
//                shape = RoundedCornerShape(12.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF2C7B76),
//                    unfocusedBorderColor = Color.LightGray
//                )
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // --- 3. SUBMIT ACTION ---
//            Button(
//                onClick = { showSuccessDialog = true },
//                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("Confirm & Send to Provider", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LabUploadResultPreview() {
//    LabUploadResultScreen(caseId = "MF-2026-X", onBack = {}, onUploadComplete = {})
//}