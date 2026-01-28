//package com.example.medifetch
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.rememberCoroutineScope
//import com.example.mymedifetch.auth.AuthRepository
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(
//    onLogin: () -> Unit = {},
//    onCreateAccount: () -> Unit = {},
//    onForgotPassword: () -> Unit = {}
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf("") }
//
//    val authRepository = remember { AuthRepository() }
//    val scope = rememberCoroutineScope()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF4FBFA))
//    ) {
//
//        Image(
//            painter = painterResource(id = R.drawable.medifetch1),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(330.dp),
//            contentScale = ContentScale.Crop
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(
//                    Color.White,
//                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
//                )
//                .padding(horizontal = 24.dp, vertical = 30.dp)
//        ) {
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text(text = "Don't have an account? ")
//                Text(
//                    text = "Create one",
//                    color = Color(0xFF0B6B45),
//                    modifier = Modifier.clickable { onCreateAccount() }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
//            )
//
//            Spacer(modifier = Modifier.height(25.dp))
//
//            Button(
//                onClick = {
//                    scope.launch {
//                        if (email.isBlank() || password.isBlank()) {
//                            errorMessage = "Email and password cannot be empty"
//                            return@launch
//                        }
//
//                        val result = authRepository.login(email, password)
//
//                        if (result.isSuccess) {
//                            onLogin()
//                        } else {
//                            errorMessage =
//                                result.exceptionOrNull()?.message ?: "Login failed"
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(54.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2C7B76)
//                )
//            ) {
//                Text("Login", color = Color.White)
//            }
//
//            if (errorMessage.isNotEmpty()) {
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = errorMessage,
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Text(
//                text = "Forgot password?",
//                color = Color(0xFF6F7B83),
//                modifier = Modifier.clickable { onForgotPassword() }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginPreview() {
//    LoginScreen()
//}
//
//
