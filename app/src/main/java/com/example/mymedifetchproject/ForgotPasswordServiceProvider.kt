////package com.example.mymedifetchproject
//
//package com.example.mymedifetchproject.provider
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Business
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.mymedifetchproject.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProviderForgotPasswordScreen(onBack: () -> Unit) {
//    var email by remember { mutableStateOf("") }
//    var isSent by remember { mutableStateOf(false) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Shared professional medical background
//        Image(
//            painter = painterResource(id = R.drawable.medical2),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .padding(24.dp)
//                .background(Color.White, RoundedCornerShape(30.dp))
//                .padding(32.dp)
//                .align(Alignment.Center)
//                .fillMaxWidth()
//        ) {
//            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF2C7B76))
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF2C7B76), modifier = Modifier.size(28.dp))
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Provider Recovery",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF2C7B76)
//                )
//            }
//
//            Text(
//                text = "Enter your facility email to reset your medical portal credentials.",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                modifier = Modifier.padding(vertical = 12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            if (!isSent) {
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Facility Email Address") },
//                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color(0xFF2C7B76),
//                        focusedLabelColor = Color(0xFF2C7B76)
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(32.dp))
//
//                Button(
//                    onClick = { isSent = true },
//                    modifier = Modifier.fillMaxWidth().height(56.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
//                    enabled = email.isNotEmpty()
//                ) {
//                    Text("Request Reset Link", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                }
//            } else {
//                // Success State
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = "✅ Facility Email Verified",
//                        color = Color(0xFF2C7B76),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "We've sent recovery instructions to your inbox. Please follow the link to secure your account.",
//                        color = Color.DarkGray,
//                        fontSize = 14.sp,
//                        textAlign = TextAlign.Center
//                    )
//
//                    Spacer(modifier = Modifier.height(32.dp))
//
//                    TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
//                        Text("Back to Provider Login", color = Color.Gray, fontWeight = FontWeight.SemiBold)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ProviderForgotPasswordPreview() {
//    ProviderForgotPasswordScreen(onBack = {})
//}
//
