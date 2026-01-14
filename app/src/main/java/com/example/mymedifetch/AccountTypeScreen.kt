package com.example.mymedifetch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymedifetch.R

@Composable
fun ChooseAccountTypeScreen(
    onNavigate: () -> Unit,
    onBack: () -> Unit = {}
    ) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    )

    Image(
        painter = painterResource(id = R.drawable.medicals),
        contentDescription = "Background Image",
        contentScale = ContentScale.Crop,
        modifier =  Modifier.fillMaxSize()
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "← Back",
            modifier = Modifier
                .clickable { onBack() }
                .padding(8.dp),
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Continue as...",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Choose your account type",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Patient Card
        AccountOptionCard(
            icon = android.R.drawable.ic_menu_info_details, // placeholder icon
            title = "Patient",
            description = "Access your medical records and find healthcare providers",
            onClick = onNavigate
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Service Provider Card
        AccountOptionCard(
            icon = android.R.drawable.ic_menu_manage, // placeholder icon
            title = "Service Provider",
            description = "Manage your facility and connect with patients",
            onClick = onNavigate
        )
    }
}

@Composable
fun AccountOptionCard(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                Modifier
                    .size(64.dp)
                    .background(
                        color = Color(0xFFD4E3E5),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // vertical spacing in Column

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun ChooseAccountTypeScreenPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ChooseAccountTypeScreen(onNavigate = {})
    }
}
