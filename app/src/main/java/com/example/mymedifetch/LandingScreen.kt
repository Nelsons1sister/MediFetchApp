

package com.example.mymedifetch

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LandingScreen(
    onNavigate: () -> Unit = {},
    onNavigateBack: () -> Unit = {} // default lambda for Preview
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(id = R.drawable.medicals),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Back Button
            Text(
                text = "← Back",
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onNavigateBack() }
                    .padding(8.dp),
                color = Color.Blue
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logo Card
            Card(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .size(120.dp)
                    .shadow(10.dp, RoundedCornerShape(18.dp))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medifetch1),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "MediFetch",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Text(
                text = "Your Healthcare, Simplified",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 6.dp),
                color = Color(0xFF6F7B83)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Connect with hospitals, labs, and pharmacies. Access your medical records anytime, anywhere.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onNavigate() },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7B76)),
                modifier = Modifier
                    .height(54.dp)
                    .widthIn(min = 220.dp)
            ) {
                Text(
                    text = "GET STARTED",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFE2EAEC)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LandingScreenPreview() {
    LandingScreen()
}