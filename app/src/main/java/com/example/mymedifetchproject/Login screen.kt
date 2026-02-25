package com.example.mymedifetchproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    role: String = "patient",
    isDarkMode: Boolean = false, // ✅ Added theme support
    onLoginSuccess: () -> Unit = {},
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    // --- 1. DYNAMIC COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE5E5E5)
    val formBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.LightGray else Color.Gray
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C7B76)
    val fieldContainer = if (isDarkMode) Color(0xFF2C2C2C) else Color.Transparent

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor) // Use dynamic background
    ) {
        // --- Back Arrow ---
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

        // --- Logo Section ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.medical1),
                contentDescription = "MediFetch Logo",
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )
        }

        // --- Bottom Form Section ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.BottomCenter)
                .background(
                    formBg, // Use dynamic form background
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            val displayRole = if (role == "provider") "Service Provider" else "Patient"

            Text(
                text = "Welcome Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = accentTeal
            )

            Text(
                text = "Sign in as $displayRole",
                fontSize = 16.sp,
                color = secondaryText,
                fontWeight = FontWeight.Medium
            )

            Row(modifier = Modifier.padding(top = 12.dp)) {
                Text(text = "Wrong account type? ", color = primaryText, fontSize = 14.sp)
                Text(
                    text = "Change",
                    color = accentTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Updated TextField with visibility fixes ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    focusedContainerColor = fieldContainer,
                    unfocusedContainerColor = fieldContainer,
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = secondaryText,
                    focusedLabelColor = accentTeal,
                    unfocusedLabelColor = secondaryText
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = accentTeal)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText,
                    focusedContainerColor = fieldContainer,
                    unfocusedContainerColor = fieldContainer,
                    focusedBorderColor = accentTeal,
                    unfocusedBorderColor = secondaryText,
                    focusedLabelColor = accentTeal,
                    unfocusedLabelColor = secondaryText
                )
            )

            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { onForgotPassword() },
                color = accentTeal,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewDark() {
    LoginScreen(role = "provider", isDarkMode = true)
}