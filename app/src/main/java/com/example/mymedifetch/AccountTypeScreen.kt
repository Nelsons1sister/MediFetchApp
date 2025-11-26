package com.example.mymedifetch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChooseAccountTypeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            icon = R.drawable.account,
            title = "Patient",
            description = "Access your medical records and find healthcare providers"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Service Provider Card
        AccountOptionCard(
            icon = R.drawable.logo,
            title = "Service Provider",
            description = "Manage your facility and connect with patients"
        )
    }
}

@Composable
fun AccountOptionCard(
    icon: Int,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun ChooseAccountTypeScreenPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ChooseAccountTypeScreen()   // ✔ FIXED
    }
}
