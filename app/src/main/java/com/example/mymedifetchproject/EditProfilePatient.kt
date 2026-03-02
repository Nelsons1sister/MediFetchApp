package com.example.mymedifetchproject.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePatientScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val profile by authViewModel.userProfile
    val isLoading by authViewModel.isLoading

    // Form States - Initialize with profile data or empty string
    var fullName by remember { mutableStateOf(profile?.full_name ?: "") }
    var phoneNumber by remember { mutableStateOf(profile?.phone_number ?: "") }

    // --- 🎨 THEME ADAPTATION ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F4F4)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val textColor = if (isDarkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = accentTeal
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustration Header
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(accentTeal.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = accentTeal
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- INPUT FIELDS ---
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentTeal,
                unfocusedBorderColor = if (isDarkMode) Color.DarkGray else Color.LightGray,
                focusedLabelColor = accentTeal,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = accentTeal
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = accentTeal) },
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = accentTeal) },
                shape = RoundedCornerShape(16.dp),
                colors = fieldColors,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- SAVE BUTTON ---
            Button(
                onClick = {
                    // ✅ Updated to match AuthViewModel 'saveProfile' logic
                    authViewModel.saveProfile(fullName, phoneNumber) { success ->
                        if (success) {
                            onBack() // Navigate back only if save was successful
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                enabled = !isLoading && fullName.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SAVE CHANGES", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }
        }
    }
}

// --- 🖼️ PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenLightPreview() {
    Surface {
        EditProfilePatientScreen(
            isDarkMode = false,
            onBack = {}
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenDarkPreview() {
    Surface {
        EditProfilePatientScreen(
            isDarkMode = true,
            onBack = {}
        )
    }
}