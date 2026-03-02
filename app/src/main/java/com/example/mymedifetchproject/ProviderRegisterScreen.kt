package com.example.mymedifetchproject.provider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymedifetchproject.R
import com.example.mymedifetchproject.data.AuthViewModel
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

@Composable
fun ProviderRegisterScreen(
    role: String,
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onAccountCreated: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBack: () -> Unit
) {
    // --- FORM STATE ---
    var facilityName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- VIEWMODEL STATE ---
    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage

    // --- THEME COLORS ---
    val topBgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F4F4)
    val sheetColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val primaryTeal = if (isDarkMode) Color(0xFF4DB6AC) else Color(0xFF2C5E5A)
    val inputTextColor = if (isDarkMode) Color.White else Color.Black
    val labelColor = if (isDarkMode) Color.Gray else Color(0xFF757575)

    val isFormValid = email.isNotEmpty() &&
            password.length >= 6 &&
            facilityName.isNotEmpty() &&
            phoneNumber.isNotEmpty()

    // --- SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Registration Successful", fontWeight = FontWeight.Bold) },
            text = { Text("The facility '$facilityName' has been registered successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onAccountCreated()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryTeal)
                ) {
                    Text("Proceed to Login", color = Color.White)
                }
            },
            containerColor = sheetColor,
            titleContentColor = inputTextColor,
            textContentColor = labelColor
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(topBgColor)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // --- HEADER ---
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                        .align(Alignment.TopStart)
                        .background(sheetColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = primaryTeal)
                }

                Image(
                    painter = painterResource(id = R.drawable.medical1),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp).align(Alignment.Center)
                )
            }

            // --- REGISTRATION FORM ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(sheetColor)
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Provider Registration", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = primaryTeal)

                val loginText = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = primaryTeal, fontWeight = FontWeight.Bold)) {
                        append("Login")
                    }
                }
                Text(
                    text = loginText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp).clickable { onNavigateToLogin() }
                )

                // --- ERROR DISPLAY ---
                errorMessage?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(12.dp))
                    }
                }

                val fieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryTeal,
                    unfocusedBorderColor = labelColor.copy(alpha = 0.5f),
                    focusedTextColor = inputTextColor,
                    unfocusedTextColor = inputTextColor
                )

                // --- INPUTS WITH AUTO-CLEAR LOGIC ---
                OutlinedTextField(
                    value = facilityName,
                    onValueChange = {
                        facilityName = it
                        if (errorMessage != null) authViewModel.clearError() // ✅ Clears error on type
                    },
                    label = { Text("Facility / Clinic Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    enabled = !isLoading,
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (errorMessage != null) authViewModel.clearError() // ✅ Clears error on type
                    },
                    label = { Text("Work Email") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    enabled = !isLoading,
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (errorMessage != null) authViewModel.clearError() // ✅ Clears error on type
                    },
                    label = { Text("Create Password") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    enabled = !isLoading,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                        }
                    }
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        if (errorMessage != null) authViewModel.clearError() // ✅ Clears error on type
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // --- ACTION BUTTON ---
                Button(
                    onClick = {
                        authViewModel.signUp(email, password, facilityName, phoneNumber, role) {
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryTeal),
                    enabled = !isLoading && isFormValid
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text("REGISTER FACILITY", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderRegisterPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderRegisterScreen("provider", viewModel(), false, {}, {}, {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ProviderRegisterPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderRegisterScreen("provider", viewModel(), true, {}, {}, {})
    }
}