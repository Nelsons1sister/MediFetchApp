//package com.example.medifetch
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Description
//import androidx.compose.material.icons.filled.HealthAndSafety
//import androidx.compose.material.icons.filled.LocalHospital
//import androidx.compose.material.icons.filled.MedicalServices
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.tooling.preview.Preview
//
//// Enum to represent bottom tabs
//enum class BottomTab(val label: String, val icon: ImageVector) {
//    Reports("Reports", Icons.Filled.Description),
//    Hospitals("Hospitals", Icons.Filled.LocalHospital),
//    Prescription("Prescription", Icons.Filled.MedicalServices),
//    Consult("Consult", Icons.Filled.HealthAndSafety)
//}
//
//@Composable
//fun DashboardScreen(
//    onMyReports: () -> Unit = {},
//    onFindHospitals: () -> Unit = {},
//    onPrescriptions: () -> Unit = {},
//    onConsultDoctor: () -> Unit = {}
//) {
//    var selectedTab by remember { mutableStateOf(BottomTab.Reports) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            // Top Bar
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "MediFetch",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = Color(0xFF0A6CFF)
//                )
//
//                IconButton(onClick = { /* profile click */ }) {
//                    Icon(
//                        imageVector = Icons.Filled.Person,
//                        contentDescription = "Profile",
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Text(
//                text = "Hello Johnson 👋",
//                style = MaterialTheme.typography.headlineSmall,
//                color = Color.Black
//            )
//            Text(
//                text = "How can we help you today?",
//                style = MaterialTheme.typography.bodyLarge,
//                color = Color.Gray
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // QUICK ACTIONS GRID
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                contentPadding = PaddingValues(8.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                item {
//                    DashboardCard(
//                        title = "My Reports",
//                        icon = Icons.Filled.Description,
//                        onClick = onMyReports
//                    )
//                }
//                item {
//                    DashboardCard(
//                        title = "Find Hospitals",
//                        icon = Icons.Filled.LocalHospital,
//                        onClick = onFindHospitals
//                    )
//                }
//                item {
//                    DashboardCard(
//                        title = "Prescriptions",
//                        icon = Icons.Filled.MedicalServices,
//                        onClick = onPrescriptions
//                    )
//                }
//                item {
//                    DashboardCard(
//                        title = "Consult Doctor",
//                        icon = Icons.Filled.HealthAndSafety,
//                        onClick = onConsultDoctor
//                    )
//                }
//            }
//        }
//
//        // Bottom Navigation Bar
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(Color.White)
//                .padding(vertical = 12.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            BottomTab.values().forEach { tab ->
//                BottomNavItem(
//                    icon = tab.icon,
//                    label = tab.label,
//                    isSelected = tab == selectedTab,
//                    onClick = { selectedTab = tab }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun DashboardCard(title: String, icon: ImageVector, onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .aspectRatio(1f)
//            .clip(RoundedCornerShape(16.dp))
//            .background(Color.White)
//            .clickable { onClick() }
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(
//                imageVector = icon,
//                contentDescription = title,
//                modifier = Modifier.size(40.dp),
//                tint = Color(0xFF0A6CFF)
//            )
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = title,
//                style = MaterialTheme.typography.bodyLarge,
//                color = Color.Black
//            )
//        }
//    }
//}
//
//@Composable
//fun BottomNavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
//    val tintColor = if (isSelected) Color(0xFF0A6CFF) else Color.Gray
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.clickable { onClick() }
//    ) {
//        Icon(imageVector = icon, contentDescription = label, tint = tintColor)
//        Text(text = label, style = MaterialTheme.typography.bodySmall, color = tintColor)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DashboardPreview() {
//    DashboardScreen()
//}
//
//
