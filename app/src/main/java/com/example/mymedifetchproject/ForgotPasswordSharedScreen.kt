package com.example.mymedifetchproject.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel = viewModel(),
    onBack: () -> Unit,
    isDarkMode: Boolean = false
) {
    // --- COORDINATED COLORS ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val mainBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val secondaryText = if (isDarkMode) Color.LightGray else Color.Gray

    var email by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BACK BUTTON ---
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(if (isDarkMode) Color(0xFF2C2C2C) else Color.White, CircleShape)
                    .size(45.dp)
                    .shadow(2.dp, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = mainBlue)
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))

        // --- HEADER ---
        Text(
            text = "Recover Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = mainBlue,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "Enter your registered email to receive a password reset link.",
            fontSize = 16.sp,
            color = secondaryText,
            modifier = Modifier.align(Alignment.Start).padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- THE FLOATING CARD ---
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
                if (!isSent) {
                    // INPUT STATE
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = { Text("Email Address") },
                        isError = errorMessage != null,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = mainBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = mainBlue,
                            focusedLabelColor = mainBlue,
                            unfocusedTextColor = if (isDarkMode) Color.White else Color.Black,
                            focusedTextColor = if (isDarkMode) Color.White else Color.Black
                        )
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Start).padding(top = 4.dp, start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            authViewModel.sendPasswordReset(email) { success, error ->
                                isLoading = false
                                if (success) isSent = true else errorMessage = error
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                        enabled = email.isNotEmpty() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("SEND RESET LINK", fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                    }
                } else {
                    // SUCCESS STATE
                    Column(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✅ Email Sent!",
                            color = mainBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Please check your inbox at $email for instructions to secure your account.",
                            color = secondaryText,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        TextButton(onClick = onBack) {
                            Text("Return to Login", color = mainBlue, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.3f))
    }
}

// --- DUAL MODE PREVIEWS ---

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun ForgotPasswordLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ForgotPasswordScreen(onBack = {}, isDarkMode = false)
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun ForgotPasswordDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ForgotPasswordScreen(onBack = {}, isDarkMode = true)
    }
}