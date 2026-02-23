package com.example.mymedifetchproject.ui.theme

import androidx.compose.ui.graphics.Color

// --- 1. Brand Colors (MediFetch Teal) ---
val MediFetchTeal = Color(0xFF2C7B76)
val MediFetchTealLight = Color(0xFF4DB6AC) // Better contrast for Dark Mode
val MediFetchTealContainer = Color(0xFFE0EDED) // Used for selection indicators

// --- 2. Light Mode Neutrals ---
val LightBackground = Color(0xFFF8FBFB)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF1A1C1E) // Near black for text

// --- 3. Dark Mode Neutrals ---
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkSurfaceVariant = Color(0xFF2C2C2C)
val DarkOnSurface = Color(0xFFE2E2E6) // Off-white for text

// --- 4. Medical Utility Colors (Optional but recommended) ---
val MedicalError = Color(0xFFBA1A1A)   // For critical results or errors
val MedicalSuccess = Color(0xFF2E7D32) // For normal results
val MedicalWarning = Color(0xFFF9A825) // For pending/waiting status