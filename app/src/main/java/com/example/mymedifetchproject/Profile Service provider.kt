package com.example.mymedifetchproject.provider

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
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun ProviderProfileScreen(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onLogout: () -> Unit = {}
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
            text = { Text("Are you sure you want to log out of the facility? You will be returned to the landing page.") },
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
            containerColor = cardBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "Facility Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Manage your clinic information and credentials",
                color = secondaryText,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        // 1. Provider/Clinic Photo & Identity
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
                        Text("RD", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Dr. Smith", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = primaryText)
                    Text("Riverside Diagnostics", color = accentTeal, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text("License: MED-9920-X", color = secondaryText, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { /* Handle update */ },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, accentTeal.copy(alpha = 0.5f))
                    ) {
                        Text("Update Facility Logo", color = accentTeal)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 2. Theme Toggle
        item {
            ProviderSectionCard(title = "Display Preferences", cardBg = cardBg, accentColor = accentTeal, isDarkMode = isDarkMode) {
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
                        colors = SwitchDefaults.colors(checkedTrackColor = accentTeal)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Operational Details
        item {
            ProviderSectionCard(title = "Operational Details", cardBg = cardBg, accentColor = accentTeal, isDarkMode = isDarkMode) {
                ProviderInfoRow(icon = Icons.Default.LocationOn, text = "123 Medical Way, Lagos, Nigeria", textColor = primaryText, iconColor = secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.2f))
                ProviderInfoRow(icon = Icons.Default.Email, text = "contact@riversidediag.com", textColor = primaryText, iconColor = secondaryText)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = secondaryText.copy(alpha = 0.2f))
                ProviderInfoRow(icon = Icons.Default.Phone, text = "+234 700 MEDI FETCH", textColor = primaryText, iconColor = secondaryText)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 4. Logout
        item {
            Button(
                onClick = { /* Verify */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Verify Medical Credentials", fontWeight = FontWeight.Bold, color = Color.White)
            }

            TextButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Log Out of Facility", color = Color.Red, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProviderSectionCard(title: String, cardBg: Color, accentColor: Color, isDarkMode: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = accentColor)
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun ProviderInfoRow(icon: ImageVector, text: String, textColor: Color, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 15.sp, color = textColor)
    }
}

// --- PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ProviderProfileLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderProfileScreen(isDarkMode = false, onThemeToggle = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun ProviderProfileDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderProfileScreen(isDarkMode = true, onThemeToggle = {})
    }
}