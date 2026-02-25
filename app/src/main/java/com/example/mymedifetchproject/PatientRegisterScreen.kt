package com.example.mymedifetchproject.patient

import com.example.mymedifetchproject.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PatientRegisterScreen(
    isDarkMode: Boolean = false,      // ✅ Added theme support
    onAccountCreated: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    // --- 1. DYNAMIC THEME VARIABLES ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE5E5E5)
    val formBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.LightGray else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val fieldContainer = if (isDarkMode) Color(0xFF2C2C2C) else Color.Transparent

    // --- State Management ---
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor) // ✅ Adaptive Background
    ) {
        // --- Back Button (Top Left) ---
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .background(
                    if (isDarkMode) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.5f),
                    RoundedCornerShape(12.dp)
                )
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = accentTeal)
        }

        // 1. Logo Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.32f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.medical1),
                contentDescription = "MediFetch Logo",
                modifier = Modifier.size(160.dp)
            )
        }

        // 2. Form Section (Adaptive Card)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter)
                .background(
                    color = formBg, // ✅ Adaptive Card Background
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(32.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Patient Registration",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = accentTeal
            )
            Text(
                text = "Create your personal health account",
                color = secondaryText,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Common Field Colors Helper ---
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = primaryText,
                unfocusedTextColor = primaryText,
                focusedContainerColor = fieldContainer,
                unfocusedContainerColor = fieldContainer,
                focusedBorderColor = accentTeal,
                unfocusedBorderColor = secondaryText,
                focusedLabelColor = accentTeal,
                unfocusedLabelColor = secondaryText
            )

            // --- Input Fields ---
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Personal Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Visibility",
                            tint = accentTeal
                        )
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Register Button ---
            Button(
                onClick = onAccountCreated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
            ) {
                Text(
                    text = "REGISTER",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- LOGIN REDIRECT ---
            TextButton(
                onClick = onAccountCreated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account? ", color = secondaryText, fontSize = 14.sp)
                    Text("Log In", color = accentTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PatientRegisterDarkPreview() {
    PatientRegisterScreen(isDarkMode = true)
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PatientRegisterLightPreview() {
    PatientRegisterScreen(isDarkMode = false)
}