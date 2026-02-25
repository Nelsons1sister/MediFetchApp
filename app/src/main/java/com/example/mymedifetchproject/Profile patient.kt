package com.example.mymedifetchproject.patient

import androidx.compose.foundation.BorderStroke
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

@Composable
fun PatientProfileScreen(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // --- 1. DYNAMIC THEME PALETTE ---
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)

    // --- LOGOUT CONFIRMATION DIALOG ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout Confirmation", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out? Your health data is protected and will require sign-in to access again.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = secondaryText)
                }
            },
            shape = RoundedCornerShape(16.dp),
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
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "My Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Manage your account and preferences",
                color = secondaryText,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        // --- PROFILE PHOTO & IDENTITY ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
                border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(accentTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("DJ", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("David John", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = primaryText)
                    Text("Patient ID: MF-2026-001", color = secondaryText, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { /* Implement Image Picker */ },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, accentTeal.copy(alpha = 0.5f))
                    ) {
                        Text(text = "Change Photo", color = accentTeal)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- THEME & PREFERENCES ---
        item {
            SectionCard(title = "Display Preferences", cardBg = cardBg, accentColor = accentTeal, isDarkMode = isDarkMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = accentTeal
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Dark Mode", color = primaryText)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onThemeToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = accentTeal,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- CONTACT INFORMATION ---
        item {
            SectionCard(title = "Contact Information", cardBg = cardBg, accentColor = accentTeal, isDarkMode = isDarkMode) {
                InfoRow(icon = Icons.Default.Email, text = "david.john@medifetch.com", textColor = primaryText, iconColor = secondaryText)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = secondaryText.copy(alpha = 0.2f)
                )
                InfoRow(icon = Icons.Default.Phone, text = "+234 801 234 5678", textColor = primaryText, iconColor = secondaryText)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- ACCOUNT ACTIONS ---
        item {
            Button(
                onClick = { /* Navigate to Edit */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Text(text = "Edit Profile Details", fontWeight = FontWeight.Bold, color = Color.White)
            }

            TextButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- SUB-COMPONENTS ---

@Composable
fun SectionCard(title: String, cardBg: Color, accentColor: Color, isDarkMode: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = accentColor,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String, textColor: Color, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 15.sp, color = textColor)
    }
}

// --- 3. DUAL PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfileLight() {
    PatientProfileScreen(
        isDarkMode = false,
        onThemeToggle = {},
        onLogout = {}
    )
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfileDark() {
    PatientProfileScreen(
        isDarkMode = true,
        onThemeToggle = {},
        onLogout = {}
    )
}