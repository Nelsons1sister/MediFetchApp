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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val profile by authViewModel.userProfile

    // ✅ State management: Updates when profile data arrives
    var fullName by remember(profile) { mutableStateOf(profile?.full_name ?: "") }
    var phoneNumber by remember(profile) { mutableStateOf(profile?.phone_number ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    // ✅ NEW LOGIC: Check if the user has actually modified the text
    val hasChanged = fullName != (profile?.full_name ?: "") || phoneNumber != (profile?.phone_number ?: "")

    // --- EMBEDDED DASHBOARD THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    // Logic for button color: Gray if no changes, Blue if changes made
    val buttonBgColor = if (hasChanged) accentBlue else secondaryText.copy(alpha = 0.4f)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (profile?.user_type == "provider") "Edit Facility Profile" else "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = accentBlue
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
            Text(
                text = if (profile?.user_type == "provider")
                    "Update your facility details so patients can reach you easily."
                else "Update your personal information to keep your records accurate.",
                color = secondaryText,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- NAME FIELD ---
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(if (profile?.user_type == "provider") "Facility / Clinic Name" else "Full Name") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = if (profile?.user_type == "provider") Icons.Default.Business else Icons.Default.Badge,
                        contentDescription = null,
                        tint = accentBlue
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentBlue,
                    unfocusedBorderColor = secondaryText.copy(alpha = 0.5f),
                    focusedLabelColor = accentBlue,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
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
                    Icon(Icons.Default.Phone, contentDescription = null, tint = accentBlue)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentBlue,
                    unfocusedBorderColor = secondaryText.copy(alpha = 0.5f),
                    focusedLabelColor = accentBlue,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- SAVE ACTION BUTTON ---
            Button(
                onClick = {
                    isSaving = true
                    authViewModel.saveProfile(fullName, phoneNumber) { success ->
                        isSaving = false
                        if (success) {
                            // After saving, we return to the profile screen
                            onBack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                // ✅ BUTTON COLOR LOGIC: Gray until modified, then Blue
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBgColor,
                    disabledContainerColor = secondaryText.copy(alpha = 0.2f)
                ),
                // ✅ BUTTON ENABLED LOGIC: Only enabled if changes exist and name isn't blank
                enabled = !isSaving && hasChanged && fullName.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = if (hasChanged) "SAVE CHANGES" else "NO CHANGES",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}