//package com.example.mymedifetchproject

package com.example.mymedifetchproject.provider



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderUploadScreen() {
    var patientId by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isFileSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FBFB))
            .padding(24.dp)
    ) {
        Text("Upload Results", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Link diagnostic files to patient records", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // 1. Patient Identification
        Text("Patient ID", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = patientId,
            onValueChange = { patientId = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. MF-2026-001") },
            leadingIcon = { Icon(Icons.Default.PersonSearch, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. File Upload Area (Simulated)
        Text("Diagnostic File", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(if (isFileSelected) Color(0xFFE8F5E9) else Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF2C7B76).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (isFileSelected) Icons.Default.Description else Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = Color(0xFF2C7B76),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isFileSelected) "malaria_test_results.pdf" else "Tap to upload PDF or Image",
                    color = if (isFileSelected) Color(0xFF2C7B76) else Color.Gray,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isFileSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (!isFileSelected) {
                    TextButton(onClick = { isFileSelected = true }) {
                        Text("Select File")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Provider Notes
        Text("Clinical Observations (Optional)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            placeholder = { Text("Add any comments for the doctor or patient...") },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.weight(1f))

        // 4. Submit Button
        Button(
            onClick = { /* Logic to send to Backend */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
            enabled = patientId.isNotEmpty() && isFileSelected
        ) {
            Text("Send to Patient", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProviderUploadPreview() {
    ProviderUploadScreen()
}