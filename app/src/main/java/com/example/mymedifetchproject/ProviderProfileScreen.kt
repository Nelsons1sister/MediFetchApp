package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.patient.SectionCard
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun ProviderProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val profile by authViewModel.userProfile
    val currentUser = authViewModel.currentUser

    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    // Ensure the profile is fetched whenever this screen is viewed
    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    // --- LOGOUT CONFIRMATION DIALOG ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red)
                    Spacer(Modifier.width(8.dp))
                    Text("Confirm Logout", fontWeight = FontWeight.Bold)
                }
            },
            text = { Text("Are you sure you want to sign out? Access to the medical portal will be restricted until you log back in.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        authViewModel.logout(onLogout)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Yes, Logout", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = primaryText)
                }
            },
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText,
            shape = RoundedCornerShape(20.dp)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- HEADER SECTION (Avatar & Name) ---
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dynamic Icon based on Role
                val roleIcon = if (profile?.role == "LAB_TECHNICIAN") Icons.Default.Science else Icons.Default.MedicalServices

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(accentBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = roleIcon,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = accentBlue
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = profile?.full_name ?: "Medical Staff",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText
                )
                val displayRole = profile?.role?.replace("_", " ")?.uppercase() ?: "VERIFYING..."
                Text(
                    text = displayRole,
                    color = accentBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        // --- DETAILS SECTION ---
        item {
            SectionCard("Personal & Facility Details", cardBg, accentBlue) {
                LocalInfoRow(Icons.Default.Badge, "Role", profile?.role?.replace("_", " ") ?: "---", primaryText, secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.1f))

                // ✅ FIXED LOGIC: Uses 'phone_number' to match AuthViewModel and Repository
                val displayPhone = profile?.phone_number ?: currentUser?.phoneNumber ?: "Not Set"
                LocalInfoRow(Icons.Default.Phone, "Phone", displayPhone, primaryText, secondaryText)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.1f))

                LocalInfoRow(Icons.Default.LocationOn, "Address", "Facility Branch HQ", primaryText, secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.1f))

                LocalInfoRow(Icons.Default.Fingerprint, "Staff ID", currentUser?.uid?.take(8)?.uppercase() ?: "---", primaryText, secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.1f))

                LocalInfoRow(Icons.Default.VerifiedUser, "Status", "Active & Verified", primaryText, secondaryText)
            }
        }

        // --- SETTINGS SECTION ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionCard("Settings", cardBg, accentBlue) {
                TextButton(
                    onClick = onEditProfile,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Edit, null, tint = accentBlue, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(16.dp))
                        Text("Edit Profile Info", color = primaryText, fontWeight = FontWeight.Medium)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.1f))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = accentBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text("Dark Mode", color = primaryText, fontWeight = FontWeight.Medium)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeToggle(it) },
                        colors = SwitchDefaults.colors(checkedTrackColor = accentBlue)
                    )
                }
            }
        }

        // --- LOGOUT BUTTON ---
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(12.dp))
                Text("LOGOUT FACILITY", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun LocalInfoRow(icon: ImageVector, label: String, value: String, textColor: Color, labelColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = labelColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = labelColor)
            Text(text = value, fontSize = 15.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

// --- DUAL MODE PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ProviderProfileLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderProfileScreen(
            isDarkMode = false,
            onThemeToggle = {},
            onEditProfile = {},
            onLogout = {}
        )
    }
}

@Preview(name = "Dark Mode", showSystemUi = true)
@Composable
fun ProviderProfileDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderProfileScreen(
            isDarkMode = true,
            onThemeToggle = {},
            onEditProfile = {},
            onLogout = {}
        )
    }
}