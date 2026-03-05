package com.example.mymedifetchproject.medifetch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    onRegister: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    // --- 1. DYNAMIC COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE3F2FD)
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color(0xFF0D47A1)
    val secondaryText = if (isDarkMode) Color.LightGray else Color(0xFF1976D2).copy(alpha = 0.7f)
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF1976D2)
    val fieldBorder = if (isDarkMode) Color.Gray else Color.LightGray

    // --- 2. STATE MANAGEMENT ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage

    // ✅ EMBEDDED: Field-specific error state
    val fieldErrors by authViewModel.fieldErrors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 3. FLOATING BACK BUTTON ---
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(if (isDarkMode) Color(0xFF2C2C2C) else Color.White, CircleShape)
                    .size(45.dp)
                    .shadow(4.dp, CircleShape)
            ) {
                Text(text = "←", fontSize = 24.sp, color = primaryText, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // --- 4. HEADER ---
        Text(
            text = "Welcome Back",
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = primaryText,
            modifier = Modifier.align(Alignment.Start)
        )

        val displayRole = when(role) {
            "provider" -> "Professional"
            "labtech" -> "Lab Technician"
            else -> "Patient"
        }

        Text(
            text = "Sign in to your $displayRole account",
            fontSize = 16.sp,
            color = secondaryText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- 5. CENTERED FORM CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(15.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Global Error Message (Firebase/Role Mismatch)
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Email Field with Inline Error Logic
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        authViewModel.clearFieldError("email") // ✅ Clear error as user fixes it
                    },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("email"), // ✅ Turns border red
                    supportingText = {
                        if (fieldErrors.containsKey("email")) {
                            Text(fieldErrors["email"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentBlue,
                        unfocusedBorderColor = fieldBorder,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = accentBlue,
                        unfocusedLabelColor = secondaryText,
                        focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                        unfocusedTextColor = if (isDarkMode) Color.White else Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field with Inline Error Logic
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        authViewModel.clearFieldError("password") // ✅ Clear error as user fixes it
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("password"), // ✅ Turns border red
                    supportingText = {
                        if (fieldErrors.containsKey("password")) {
                            Text(fieldErrors["password"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = accentBlue)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentBlue,
                        unfocusedBorderColor = fieldBorder,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = accentBlue,
                        unfocusedLabelColor = secondaryText,
                        focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                        unfocusedTextColor = if (isDarkMode) Color.White else Color.Black
                    )
                )

                Text(
                    text = "Forgot Password?",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 12.dp)
                        .clickable { onForgotPassword() },
                    color = accentBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- LOGIN BUTTON ---
                Button(
                    onClick = {
                        authViewModel.login(
                            email = email,
                            pass = password,
                            expectedRole = role, // ✅ Strict role check logic
                            onRoleFound = { verifiedRole -> onLoginSuccess(verifiedRole) }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF1976D2) else primaryText),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("SIGN IN", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // --- 6. FOOTER ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Text(text = "Wrong account type? ", color = Color.Gray, fontSize = 15.sp)
                Text(
                    text = "Go Back",
                    color = accentBlue,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { onBack() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(text = "Don't have an account? ", color = Color.Gray, fontSize = 15.sp)
                Text(
                    text = "Register",
                    color = accentBlue,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { onRegister() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ... (Keep existing imports from the previous code) ...

// --- ADDED PREVIEWS SECTION ---

@Preview(name = "Light Mode - Patient", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        // We pass a mock or new AuthViewModel for preview purposes
        LoginScreen(
            role = "patient",
            isDarkMode = false,
            authViewModel = viewModel()
        )
    }
}

@Preview(name = "Dark Mode - Provider", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        LoginScreen(
            role = "provider",
            isDarkMode = true,
            authViewModel = viewModel()
        )
    }
}

@Preview(name = "Light Mode - Lab Tech", showBackground = true, showSystemUi = true)
@Composable
fun LoginPreviewLabTech() {
    MyMedifetchProjectTheme(darkTheme = false) {
        LoginScreen(
            role = "labtech",
            isDarkMode = false,
            authViewModel = viewModel()
        )
    }
}