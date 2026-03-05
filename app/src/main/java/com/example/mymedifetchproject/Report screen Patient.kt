package com.example.mymedifetchproject.patient

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

// --- 1. DATA MODEL ---
data class PatientReport(
    val id: String,
    val title: String,
    val date: String,
    val labName: String,
    val isReady: Boolean = true
)

// --- 2. MAIN SCREEN ---
@Composable
fun ReportsScreen(
    onBack: () -> Unit,
    onReportClick: (String) -> Unit,
    isDarkMode: Boolean
) {
    // --- DYNAMIC THEME PALETTE ---
    // ✅ Updated to match the soft blue background and deep blue branding
    val bgColor = if (isDarkMode) Color.Black else Color(0xFFF5F9FF)
    val cardBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val primaryText = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFB0B0B0) else Color.Gray
    val accentBlue = if (isDarkMode) Color(0xFF64B5F6) else Color(0xFF0D47A1)

    // 🩺 MOCK DATA
    val reportsList = listOf(
        PatientReport("REP001", "Malaria & Typhoid Result", "Feb 18, 2026", "City Diagnostics"),
        PatientReport("REP002", "Full Blood Count", "Jan 12, 2026", "Savannah Health Lab"),
        PatientReport("REP003", "Urinalysis", "Dec 05, 2025", "St. Mary's Pathology")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // --- TOP BAR ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(cardBg, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = accentBlue
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Medical Reports",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText
                )
                Text(
                    text = "Access your digital results",
                    color = secondaryText,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION TITLE ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = secondaryText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recent Results",
                fontWeight = FontWeight.SemiBold,
                color = secondaryText,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- REPORTS LIST ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(reportsList) { report ->
                ReportItemCard(
                    report = report,
                    isDarkMode = isDarkMode,
                    cardBg = cardBg,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    accentBlue = accentBlue,
                    onClick = { onReportClick(report.id) }
                )
            }
        }
    }
}

// --- 3. SUB-COMPONENT ---
@Composable
fun ReportItemCard(
    report: PatientReport,
    isDarkMode: Boolean,
    cardBg: Color,
    primaryText: Color,
    secondaryText: Color,
    accentBlue: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(if (isDarkMode) 0.dp else 2.dp),
        border = if (isDarkMode) BorderStroke(1.dp, Color(0xFF222222)) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(accentBlue.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = accentBlue
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = primaryText
                )
                Text(
                    text = "${report.labName} • ${report.date}",
                    fontSize = 12.sp,
                    color = secondaryText
                )
            }

            // Download Button
            IconButton(
                onClick = { /* Logic for downloading PDF */ },
                colors = IconButtonDefaults.iconButtonColors(contentColor = accentBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download PDF",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// --- 4. PREVIEWS ---
@Preview(name = "Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun ReportsPreviewLight() {
    MyMedifetchProjectTheme(darkTheme = false) {
        ReportsScreen(onBack = {}, onReportClick = {}, isDarkMode = false)
    }
}

@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true)
@Composable
fun ReportsPreviewDark() {
    MyMedifetchProjectTheme(darkTheme = true) {
        ReportsScreen(onBack = {}, onReportClick = {}, isDarkMode = true)
    }
}