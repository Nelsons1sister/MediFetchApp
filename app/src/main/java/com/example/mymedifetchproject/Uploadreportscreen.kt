package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadResultScreen(
    caseId: String,
    onBack: () -> Unit,
    onUploadComplete: () -> Unit
) {
    var resultValue by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2C7B76),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Submission Successful", fontWeight = FontWeight.Bold) },
            text = { Text("The lab results for Case #$caseId have been uploaded. The patient and healthcare provider can now access them.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onUploadComplete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
                ) {
                    Text("Done")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Lab Results", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FBFB))
                .padding(padding)
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0EDED)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF2C7B76))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Active Case: #$caseId", fontWeight = FontWeight.Bold, color = Color(0xFF2C7B76))
                        Text("Patient: David John", fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Test Findings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = resultValue,
                onValueChange = { resultValue = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                placeholder = { Text("e.g. PCV: 35%, Malaria Parasite: ++ detected...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2C7B76),
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C7B76))
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF2C7B76))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Attach Scan/Result Slip", color = Color(0xFF2C7B76), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    isUploading = true
                    showSuccessDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 8.dp),
                enabled = resultValue.isNotEmpty() && !isUploading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit & Close Case", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

// --- PREVIEW SECTION ---

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullUploadScreenPreview() {
    UploadResultScreen(
        caseId = "MF-2026-PREVIEW",
        onBack = {},
        onUploadComplete = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SuccessDialogPreview() {
    // This allows you to preview just the dialog design
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Gray) {
        AlertDialog(
            onDismissRequest = { },
            icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2C7B76), modifier = Modifier.size(48.dp)) },
            title = { Text("Submission Successful", fontWeight = FontWeight.Bold) },
            text = { Text("The lab results for Case #MF-2026-XYZ have been uploaded.") },
            confirmButton = {
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76))) {
                    Text("Done")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}