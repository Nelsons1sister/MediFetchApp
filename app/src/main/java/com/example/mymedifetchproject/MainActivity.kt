package com.example.mymedifetchproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mymedifetchproject.ui.theme.MyMedifetchProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            // 1. THIS IS THE LOGIC: We create the state here...
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDark) }

            // 2. ...we pass it to the Theme wrapper...
            MyMedifetchProjectTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. ...and we pass both the state and the 'updater' to the NavGraph.
                    NavGraph(
                        isDarkMode = isDarkMode,
                        onThemeToggle = { newState -> isDarkMode = newState }
                    )
                }
            }
        }
    }
}