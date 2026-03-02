package com.example.mymedifetchproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun LoginScreen(
    role: String = "patient",
    authViewModel: AuthViewModel,
    isDarkMode: Boolean = false,
    onLoginSuccess: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    // --- 1. DYNAMIC COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F4F4)
    val formBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.LightGray else Color(0xFF757575)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C5E5A)
    val fieldContainer = if (isDarkMode) Color(0xFF2C2C2C) else Color.Transparent

    // --- 2. STATE MANAGEMENT ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = accentTeal)
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
                modifier = Modifier.size(160.dp),
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
                    formBg,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            val displayRole = if (role == "provider") "Service Provider" else "Patient"

            Text(
                text = "Welcome Back",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = accentTeal
            )

            Text(
                text = "Sign in as $displayRole",
                fontSize = 15.sp,
                color = secondaryText,
                fontWeight = FontWeight.Medium
            )

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "Wrong account type? ", color = secondaryText, fontSize = 14.sp)
                Text(
                    text = "Change",
                    color = accentTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ NEW: Error Message Card (Visual Feedback)
            errorMessage?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // --- Input Fields ---
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = accentTeal)
                    }
                },
                colors = textFieldColors
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

            // --- LOGIN BUTTON ---
            Button(
                onClick = {
                    authViewModel.login(
                        email = email,
                        pass = password,
                        onRoleFound = { verifiedRole ->
                            onLoginSuccess(verifiedRole)
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentTeal,
                    disabledContainerColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFE0E0E0)
                ),
                enabled = !isLoading && email.isNotEmpty() && password.length >= 6
            ) {
                if (isLoading) {
                    // ✅ VISUAL FEEDBACK: The Spinner
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("LOGIN", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- PREVIEWS ---
@Preview(name = "Login Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        LoginScreen(role = "patient", isDarkMode = false, authViewModel = viewModel())
    }
}

@Preview(name = "Login Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        LoginScreen(role = "provider", isDarkMode = true, authViewModel = viewModel())
    }
}