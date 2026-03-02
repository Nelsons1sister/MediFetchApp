package com.example.mymedifetchproject.patient

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

@Composable
fun PatientProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onEditProfile: () -> Unit, // ✅ Successfully triggers navigation
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val profile by authViewModel.userProfile
    val currentUser = authViewModel.currentUser

    // Theme logic
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF8FBFB)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)

    // Sync data on load
    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out of your health profile?") },
            confirmButton = {
                TextButton(onClick = { authViewModel.logout(onLogout) }) {
                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = secondaryText)
                }
            },
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text("My Profile", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = primaryText)
            Text("Personal health settings", color = secondaryText)
            Spacer(modifier = Modifier.height(30.dp))
        }

        // Profile Avatar Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(accentTeal), contentAlignment = Alignment.Center) {
                        val initial = (profile?.full_name ?: currentUser?.email)?.take(1)?.uppercase() ?: "U"
                        Text(initial, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = profile?.full_name ?: "Guest User", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = primaryText)
                    Text(text = "Patient ID: ${currentUser?.uid?.take(8)?.uppercase()}", color = secondaryText, fontSize = 14.sp)
                }
            }
        }

        // Contact Details Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionCard("Contact Details", cardBg, accentTeal) {
                InfoRow(Icons.Default.Email, currentUser?.email ?: "Not set", primaryText, secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.2f))
                InfoRow(Icons.Default.Phone, profile?.phone_number ?: "Update phone number", primaryText, secondaryText)
            }
        }

        // Actions Section
        item {
            Spacer(modifier = Modifier.height(32.dp))

            // ✅ THE EDIT PROFILE BUTTON
            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Text("Edit Profile Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout Account", color = Color.Red, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// --- 🛠️ HELPER COMPONENTS (Fixes the Unresolved Reference errors) ---

@Composable
fun SectionCard(
    title: String,
    containerColor: Color,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, color = accentColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String, textColor: Color, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = textColor, fontSize = 15.sp)
    }
}

// --- PREVIEWS ---

@Preview(name = "Patient - Light", showSystemUi = true)
@Composable
fun PreviewPatientLight() {
    Surface { PatientProfileScreen(isDarkMode = false, onThemeToggle = {}, onEditProfile = {}, onLogout = {}) }
}

@Preview(name = "Patient - Dark", showSystemUi = true)
@Composable
fun PreviewPatientDark() {
    Surface { PatientProfileScreen(isDarkMode = true, onThemeToggle = {}, onEditProfile = {}, onLogout = {}) }
}