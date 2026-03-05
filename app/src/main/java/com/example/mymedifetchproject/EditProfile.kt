package com.example.mymedifetchproject.patient

import androidx.compose.ui.graphics.vector.ImageVector



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
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
fun PatientEditProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val profile by authViewModel.userProfile
    val currentUser = authViewModel.currentUser

    // --- 1. STATE MANAGEMENT ---
    var fullName by remember { mutableStateOf(profile?.full_name ?: "") }
    var phoneNumber by remember { mutableStateOf(profile?.phone_number ?: "") }
    val email = currentUser?.email ?: "Not available"

    // --- 2. DYNAMIC THEME ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

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
                    titleContentColor = primaryText,
                    navigationIconContentColor = accentBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- 3. INPUT SECTION ---
            Text(
                "Update your information below to keep your medical records accurate.",
                color = secondaryText,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            EditField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                icon = Icons.Default.Badge,
                isDarkMode = isDarkMode,
                accentBlue = accentBlue,
                primaryText = primaryText
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Phone Number Field
            EditField(
                label = "Phone Number",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                icon = Icons.Default.Phone,
                isDarkMode = isDarkMode,
                accentBlue = accentBlue,
                primaryText = primaryText
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field (Read Only)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address (Permanent)") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = secondaryText.copy(alpha = 0.3f),
                    disabledLabelColor = secondaryText,
                    disabledTextColor = secondaryText
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- 4. SAVE BUTTON ---
            Button(
                onClick = {
                    // Here you would call authViewModel.updateProfile(fullName, phoneNumber)
                    // For now, we simulate success
                    onSaveSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    isDarkMode: Boolean,
    accentBlue: Color,
    primaryText: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = accentBlue) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentBlue,
            focusedLabelColor = accentBlue,
            unfocusedBorderColor = if (isDarkMode) Color.DarkGray else Color.LightGray,
            cursorColor = accentBlue,
            focusedTextColor = primaryText,
            unfocusedTextColor = primaryText
        )
    )
}

// --- PREVIEWS ---
@Preview(name = "Edit Profile - Light", showBackground = true)
@Composable
fun PreviewEditLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        PatientEditProfileScreen(isDarkMode = false, onBack = {}, onSaveSuccess = {})
    }
}

@Preview(name = "Edit Profile - Dark", showSystemUi = true)
@Composable
fun PreviewEditDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        PatientEditProfileScreen(isDarkMode = true, onBack = {}, onSaveSuccess = {})
    }
}