//package com.example.mymedifetchproject

package com.example.mymedifetchproject.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientForgotPasswordScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var isSent by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image consistent with Landing/Login
        Image(
            painter = painterResource(id = R.drawable.medical2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, RoundedCornerShape(30.dp))
                .padding(32.dp)
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF2C7B76))
            }

            Text(
                text = "Recover Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C7B76)
            )

            Text(
                text = "Enter your registered email to receive a password reset link.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isSent) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { isSent = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                    enabled = email.isNotEmpty()
                ) {
                    Text("Send Reset Link", fontWeight = FontWeight.Bold)
                }
            } else {
                // Success State within the card
                Text(
                    text = "✅ Recovery email sent! Please check your inbox (and spam folder) to reset your password.",
                    color = Color(0xFF2C7B76),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Return to Login", color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PatientForgotPasswordPreview() {
    // Providing a no-op lambda for the preview
    PatientForgotPasswordScreen(onBack = {})
}