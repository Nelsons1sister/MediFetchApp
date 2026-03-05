package com.example.mymedifetchproject.provider

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
fun ProviderRegisterScreen(
    role: String, // "provider" or "labtech"
    authViewModel: AuthViewModel = viewModel(),
    isDarkMode: Boolean,
    onAccountCreated: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBack: () -> Unit
) {
    // --- 1. COORDINATED COLOR PALETTE ---
    val bgColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE3F2FD)
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val mainBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)
    val secondaryBlue = if (isDarkMode) Color.LightGray else Color(0xFF1976D2)

    var facilityName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // DYNAMIC LABELS BASED ON ROLE
    val isLabTech = role == "labtech"
    val headerTitle = if (isLabTech) "Lab Portal" else "Create Portal"
    val subHeader = if (isLabTech) "Register your diagnostic lab." else "Register your medical facility to start."
    val facilityLabel = if (isLabTech) "Laboratory Name" else "Facility Name"
    val buttonText = if (isLabTech) "REGISTER LABORATORY" else "REGISTER FACILITY"

    val isLoading by authViewModel.isLoading
    val errorMessage by authViewModel.errorMessage
    // ✅ EMBEDDED: Access the field-specific errors
    val fieldErrors by authViewModel.fieldErrors

    // --- SUCCESS DIALOG ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Registration Successful", fontWeight = FontWeight.Bold) },
            text = { Text("The ${if(isLabTech) "laboratory" else "facility"} '$facilityName' has been registered successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onAccountCreated()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue)
                ) {
                    Text("Proceed to Login", color = Color.White)
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BACK BUTTON ---
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

        // --- HEADER ---
        Text(
            text = headerTitle,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = mainBlue,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = subHeader,
            fontSize = 16.sp,
            color = secondaryBlue.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // --- THE FLOATING FORM CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(15.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                errorMessage?.let {
                    Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                }

                val fieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = mainBlue,
                    unfocusedBorderColor = Color.LightGray,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = mainBlue,
                    focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedTextColor = if (isDarkMode) Color.White else Color.Black
                )

                // Facility/Lab Name
                OutlinedTextField(
                    value = facilityName,
                    onValueChange = {
                        facilityName = it
                        authViewModel.clearFieldError("name") // ✅ Clear specific error
                    },
                    label = { Text(facilityLabel) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    singleLine = true,
                    isError = fieldErrors.containsKey("name"),
                    supportingText = {
                        if (fieldErrors.containsKey("name")) {
                            Text(fieldErrors["name"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // Work Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        authViewModel.clearFieldError("email")
                    },
                    label = { Text("Work Email") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    singleLine = true,
                    isError = fieldErrors.containsKey("email"),
                    supportingText = {
                        if (fieldErrors.containsKey("email")) {
                            Text(fieldErrors["email"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        authViewModel.clearFieldError("password")
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    isError = fieldErrors.containsKey("password"),
                    supportingText = {
                        if (fieldErrors.containsKey("password")) {
                            Text(fieldErrors["password"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = mainBlue)
                        }
                    }
                )

                // Phone Number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        authViewModel.clearFieldError("phone")
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                    isError = fieldErrors.containsKey("phone"),
                    supportingText = {
                        if (fieldErrors.containsKey("phone")) {
                            Text(fieldErrors["phone"] ?: "", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Button(
                    onClick = { authViewModel.signUp(email, password, facilityName, phoneNumber, role) { showSuccessDialog = true } },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                    enabled = !isLoading // ✅ Enabled is handled by internal validation logic now
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(buttonText, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- FOOTER ---
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

// Previews updated with role samples
@Preview(name = "Lab Tech Register - Light", showBackground = true, showSystemUi = true)
@Composable
fun LabTechRegisterPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ProviderRegisterScreen(
            role = "labtech",
            authViewModel = viewModel(),
            isDarkMode = false,
            onAccountCreated = {},
            onNavigateToLogin = {},
            onBack = {}
        )
    }
}

@Preview(name = "Provider Register - Dark", showBackground = true, showSystemUi = true)
@Composable
fun ProviderRegisterPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ProviderRegisterScreen(
            role = "provider",
            authViewModel = viewModel(),
            isDarkMode = true,
            onAccountCreated = {},
            onNavigateToLogin = {},
            onBack = {}
        )
    }
}