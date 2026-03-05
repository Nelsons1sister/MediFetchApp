//package com.example.mymedifetchproject.provider
//
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//// --- 1. SHARED IDENTITY HEADER ---
//@Composable
//fun SharedProviderHeader(name: String, role: String, id: String, isDarkMode: Boolean, accentBlue: Color) {
//    val cardBg = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(cardBg),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
//            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = accentBlue.copy(alpha = 0.1f)) {
//                Icon(Icons.Default.Person, null, tint = accentBlue, modifier = Modifier.padding(8.dp))
//            }
//            Spacer(Modifier.width(12.dp))
//            Column {
//                Text(name, fontWeight = FontWeight.Bold, color = if(isDarkMode) Color.White else Color.Black)
//                Text("${role.uppercase()} • ID: $id", fontSize = 11.sp, color = Color.Gray)
//            }
//        }
//    }
//}
//
//// --- 2. SHARED STAT CARDS ---
//@Composable
//fun ProviderStatCard(title: String, count: String, icon: ImageVector, accentColor: Color, isDarkMode: Boolean) {
//    Card(
//        modifier = Modifier.height(100.dp),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(if (isDarkMode) Color(0xFF1E1E1E) else Color.White)
//    ) {
//        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
//            Icon(icon, null, tint = accentColor, modifier = Modifier.size(20.dp))
//            Text(title, fontSize = 12.sp, color = Color.Gray)
//            Text(count, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (isDarkMode) Color.White else Color.Black)
//        }
//    }
//}
//
//// --- 3. SHARED PROFILE CONTENT ---
//// This handles the profile UI for BOTH roles
//@Composable
//fun SharedProfileContent(
//    userName: String,
//    isDarkMode: Boolean,
//    accentBlue: Color,
//    onThemeToggle: (Boolean) -> Unit,
//    onEditProfile: () -> Unit,
//    onLogout: () -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxSize().padding(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Icon(Icons.Default.AccountCircle, null, Modifier.size(80.dp), tint = accentBlue)
//        Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if(isDarkMode) Color.White else Color.Black)
//
//        Spacer(Modifier.height(24.dp))
//
//        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(if(isDarkMode) Color(0xFF1A1A1A) else Color.White)) {
//            Column {
//                // The Edit Profile Button you requested previously
//                ProfileRowItem(Icons.Default.Edit, "Edit Profile Info", isDarkMode, onEditProfile)
//
//                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f))
//
//                Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
//                    Text("Dark Mode", color = if(isDarkMode) Color.White else Color.Black)
//                    Switch(checked = isDarkMode, onCheckedChange = onThemeToggle)
//                }
//            }
//        }
//
//        Spacer(Modifier.weight(1f))
//
//        Button(
//            onClick = onLogout,
//            modifier = Modifier.fillMaxWidth().height(50.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE))
//        ) {
//            Text("LOGOUT", color = Color.Red, fontWeight = FontWeight.Bold)
//        }
//    }
//}
//
//// Helper for Profile Rows
//@Composable
//private fun ProfileRowItem(icon: ImageVector, label: String, isDarkMode: Boolean, onClick: () -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(icon, null, tint = if (isDarkMode) Color.LightGray else Color.DarkGray)
//        Spacer(Modifier.width(16.dp))
//        Text(label, color = if (isDarkMode) Color.White else Color.Black)
//    }
//}
//
