package com.example.mymedifetchproject.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val profile by authViewModel.userProfile

    // ✅ remember(profile) ensures the local state updates once the backend data arrives
    var fullName by remember(profile) { mutableStateOf(profile?.full_name ?: "") }
    var phoneNumber by remember(profile) { mutableStateOf(profile?.phone_number ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    // --- DYNAMIC THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)

    Scaffold(
        containerColor = bgColor, // ✅ Prevents white flicker in Dark Mode
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (profile?.user_type == "provider") "Edit Facility Profile" else "Edit Profile",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = accentTeal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = primaryText
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
            // Contextual header text
            Text(
                text = if (profile?.user_type == "provider")
                    "Update your facility details so patients can reach you easily."
                else "Update your personal information to keep your records accurate.",
                color = secondaryText,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- NAME FIELD (FACILITY OR PATIENT) ---
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(if (profile?.user_type == "provider") "Facility / Clinic Name" else "Full Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = if (profile?.user_type == "provider") Icons.Default.Business else Icons.Default.Badge,
                        contentDescription = null,
                        tint = accentTeal
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = secondaryText.copy(alpha = 0.5f),
                    focusedLabelColor = accentTeal,
                    cursorColor = accentTeal,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    unfocusedLabelColor = secondaryText
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- PHONE NUMBER FIELD ---
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Contact Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = accentTeal)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = secondaryText.copy(alpha = 0.5f),
                    focusedLabelColor = accentTeal,
                    cursorColor = accentTeal,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    unfocusedLabelColor = secondaryText
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- SAVE ACTION BUTTON ---
            Button(
                onClick = {
                    isSaving = true
                    authViewModel.saveProfile(fullName, phoneNumber) { success ->
                        isSaving = false
                        if (success) onBack() // Return to Profile Screen on success
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                enabled = !isSaving && fullName.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Save Changes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// --- DUAL PREVIEWS ---
@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun EditProfileLightPreview() {
    Surface {
        EditProfileScreen(isDarkMode = false, onBack = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun EditProfileDarkPreview() {
    Surface {
        EditProfileScreen(isDarkMode = true, onBack = {})
    }
}