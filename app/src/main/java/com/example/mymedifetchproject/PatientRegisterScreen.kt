package com.example.mymedifetchproject.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
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
    // --- 1. COORDINATED COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE3F2FD)
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val mainBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val secondaryBlue = if (isDarkMode) Color.LightGray else Color(0xFF1976D2)

    // --- 2. STATE MANAGEMENT ---
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage
    // ✅ EMBEDDED: Access field-specific error state
    val fieldErrors by authViewModel.fieldErrors

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
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue)
                ) {
                    Text("Get Started", color = Color.White)
                }
            },
            containerColor = cardBg,
            titleContentColor = if (isDarkMode) Color.White else Color.Black,
            textContentColor = if (isDarkMode) Color.LightGray else Color.Gray
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 24.dp)
            .statusBarsPadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 4. FLOATING BACK BUTTON ---
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(if (isDarkMode) Color(0xFF2C2C2C) else Color.White, CircleShape)
                    .size(45.dp)
                    .shadow(2.dp, CircleShape)
            ) {
                Text(text = "←", fontSize = 24.sp, color = mainBlue, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- 5. HEADER ---
        Text(
            text = "Join MediFetch",
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = mainBlue,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = "Create your personal health account",
            fontSize = 16.sp,
            color = secondaryBlue.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // --- 6. THE FLOATING FORM CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(15.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                }

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = mainBlue,
                    unfocusedBorderColor = Color.LightGray,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = mainBlue,
                    focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedTextColor = if (isDarkMode) Color.White else Color.Black
                )

                // --- NAME FIELD ---
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        authViewModel.clearFieldError("name") // ✅ Clear error on type
                    },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("name"),
                    supportingText = {
                        if (fieldErrors.containsKey("name")) {
                            Text(fieldErrors["name"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // --- EMAIL FIELD ---
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        authViewModel.clearFieldError("email")
                    },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("email"),
                    supportingText = {
                        if (fieldErrors.containsKey("email")) {
                            Text(fieldErrors["email"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // --- PHONE FIELD ---
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        authViewModel.clearFieldError("phone")
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    singleLine = true,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("phone"),
                    supportingText = {
                        if (fieldErrors.containsKey("phone")) {
                            Text(fieldErrors["phone"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // --- PASSWORD FIELD ---
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        authViewModel.clearFieldError("password")
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    enabled = !isLoading,
                    isError = fieldErrors.containsKey("password"),
                    supportingText = {
                        if (fieldErrors.containsKey("password")) {
                            Text(fieldErrors["password"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null, tint = mainBlue)
                        }
                    }
                )

                Button(
                    onClick = {
                        // Logic handles validation internally now
                        authViewModel.signUp(email, password, name, phone, "patient") {
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text("CREATE ACCOUNT", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 7. FOOTER ---
        val loginText = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(style = SpanStyle(color = mainBlue, fontWeight = FontWeight.Black)) {
                append("Login")
            }
        }
        Text(
            text = loginText,
            fontSize = 15.sp,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// Previews remain consistent with dual-theme support
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