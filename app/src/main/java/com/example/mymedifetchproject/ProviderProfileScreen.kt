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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.patient.InfoRow
import com.example.mymedifetchproject.patient.SectionCard

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

    // --- THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)

    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout Facility", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out of the medical practice portal?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    authViewModel.logout(onLogout)
                }) {
                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = primaryText)
                }
            },
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- 1. HEADER ---
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Facility Profile", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = primaryText)
            Text("Medical Practice Administration", color = secondaryText)
            Spacer(modifier = Modifier.height(30.dp))
        }

        // --- 2. PROFILE OVERVIEW CARD ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(accentTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        val initial = (profile?.full_name ?: "F").take(1).uppercase()
                        Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = profile?.full_name ?: "Loading Facility...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = primaryText
                    )
                    Text(
                        text = "License: PRV-${currentUser?.uid?.take(6)?.uppercase() ?: "N/A"}",
                        color = accentTeal,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- 3. CONTACT INFO SECTION ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            // ✅ FIXED: Removed 'isDarkMode' to match the SectionCard definition
            SectionCard("Operational Contact", cardBg, accentTeal) {
                InfoRow(Icons.Default.Business, "Registered Facility", primaryText, secondaryText)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = secondaryText.copy(alpha = 0.2f)
                )
                InfoRow(Icons.Default.Phone, profile?.phone_number ?: "Add contact number", primaryText, secondaryText)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = secondaryText.copy(alpha = 0.2f)
                )
                InfoRow(Icons.Default.Email, profile?.email ?: "No email linked", primaryText, secondaryText)
            }
        }

        // --- 4. THEME SETTINGS SECTION ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            // ✅ FIXED: Removed 'isDarkMode' here as well
            SectionCard("App Settings", cardBg, accentTeal) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = accentTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Dark Mode", color = primaryText, fontWeight = FontWeight.Medium)
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = accentTeal,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = secondaryText.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        // --- 5. ACTION BUTTONS ---
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Update Clinic Profile", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout Facility", color = Color.Red, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(name = "Provider - Light", showSystemUi = true)
@Composable
fun PreviewProviderLight() {
    MaterialTheme {
        ProviderProfileScreen(
            isDarkMode = false,
            onThemeToggle = {},
            onEditProfile = {},
            onLogout = {}
        )
    }
}

@Preview(name = "Provider - Dark", showSystemUi = true)
@Composable
fun PreviewProviderDark() {
    MaterialTheme {
        Surface(color = Color.Black) {
            ProviderProfileScreen(
                isDarkMode = true,
                onThemeToggle = {},
                onEditProfile = {},
                onLogout = {}
            )
        }
    }
}