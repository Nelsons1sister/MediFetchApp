//package com.example.mymedifetchproject

package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSicknessScreen(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    onNavigate: (String) -> Unit = {} // 👈 Added navigation callback
) {
    var symptomText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write Health Report", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isSending) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            // 🟢 Added Bottom Navigation Bar
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false, // Not "Home" but goes there
                    onClick = { onNavigate(Screen.PatientHome.route) },
                    label = { Text("HOME", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.FindLabs.route) },
                    label = { Text("LABS", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Filled.Science, contentDescription = null) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.PatientReports.route) },
                    label = { Text("REPORTS", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Filled.Description, contentDescription = null) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* onNavigate("profile") */ },
                    label = { Text("PROFILE", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Describe how you feel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Your service provider will review this and advise on next steps.",
                color = Color(0xFF424242),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = symptomText,
                onValueChange = { symptomText = it },
                enabled = !isSending,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("E.g. I have been feeling cold and shivering since last night...") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2C7B76),
                    unfocusedContainerColor = Color(0xFFF9F9F9)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (symptomText.isNotEmpty()) {
                        isSending = true
                        scope.launch {
                            delay(1500)
                            onSubmitted()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = symptomText.isNotEmpty() && !isSending,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C7B76),
                    disabledContainerColor = Color(0xFF2C7B76).copy(alpha = 0.6f)
                )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send to Provider", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Note: This is a digital consultation. In case of emergency, please call local emergency services.",
                fontSize = 13.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportSicknessPreview() {
    ReportSicknessScreen(onBack = {}, onSubmitted = {})
}