package com.example.mymedifetchproject.patient

import com.example.mymedifetchproject.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun PatientRegisterScreen(
    authViewModel: AuthViewModel,
    isDarkMode: Boolean = false,
    onAccountCreated: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    // --- 1. THEME VARIABLES ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F4F4)
    val formBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color.LightGray else Color(0xFF757575)
    val accentTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C5E5A)
    val fieldContainer = if (isDarkMode) Color(0xFF2C2C2C) else Color.Transparent

    // --- 2. STATE MANAGEMENT ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage
    val scrollState = rememberScrollState()

    // --- 3. SUCCESS POPUP ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Account Verified", fontWeight = FontWeight.Bold) },
            text = { Text("Welcome to MediFetch, $name! Your account has been created successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onAccountCreated()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentTeal)
                ) {
                    Text("Get Started", color = Color.White)
                }
            },
            containerColor = formBg,
            titleContentColor = primaryText,
            textContentColor = secondaryText
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
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

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.35f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.medical1),
                contentDescription = "MediFetch Logo",
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .align(Alignment.BottomCenter)
                .background(formBg, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .verticalScroll(scrollState)
        ) {
            Text(text = "Join MediFetch", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = accentTeal)
            Text(text = "Create your personal health account", fontSize = 14.sp, color = secondaryText, modifier = Modifier.padding(top = 4.dp))

            Row(modifier = Modifier.padding(top = 12.dp)) {
                Text(text = "Already have an account? ", color = secondaryText, fontSize = 14.sp)
                Text(
                    text = "Login",
                    color = accentTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Error Message Display ---
            errorMessage?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(text = it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Medium)
                }
            }

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

            // ✅ REACTIVE INPUTS: clearError() added to onValueChange
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (errorMessage != null) authViewModel.clearError()
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (errorMessage != null) authViewModel.clearError()
                },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    if (errorMessage != null) authViewModel.clearError()
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (errorMessage != null) authViewModel.clearError()
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = !isLoading,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null, tint = accentTeal)
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    authViewModel.signUp(email, password, name, phone, "patient") {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentTeal),
                enabled = !isLoading && name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.length >= 6
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(text = "CREATE ACCOUNT", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- PREVIEWS ---

@Preview(name = "Patient Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun PatientRegisterLightPreview() {
    MyMedifetchProjectTheme(darkTheme = false) {
        PatientRegisterScreen(authViewModel = viewModel(), isDarkMode = false)
    }
}

@Preview(name = "Patient Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun PatientRegisterDarkPreview() {
    MyMedifetchProjectTheme(darkTheme = true) {
        PatientRegisterScreen(authViewModel = viewModel(), isDarkMode = true)
    }
}