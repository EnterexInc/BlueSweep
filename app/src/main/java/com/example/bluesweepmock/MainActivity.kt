package com.example.bluesweepmock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.bluesweepmock.ui.screens.CleanupEventScreen
import com.example.bluesweepmock.ui.screens.HomeScreen
import com.example.bluesweepmock.ui.screens.LoginScreen
import com.example.bluesweepmock.ui.screens.PollutionAwarenessScreen
import com.example.bluesweepmock.ui.screens.ProfileScreen
import com.example.bluesweepmock.ui.screens.WasteTrackingScreen
import com.example.bluesweepmock.ui.theme.BlueSweepMockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlueSweepMockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Simple state to track if user is logged in
                    var isLoggedIn by remember { mutableStateOf(false) }
                    
                    // State to track current screen
                    var currentScreen by remember { mutableStateOf("login") }
                    
                    when (currentScreen) {
                        "login" -> {
                            // Show LoginScreen when not logged in
                            LoginScreen(
                                onLoginClick = { 
                                    isLoggedIn = true
                                    currentScreen = "home"
                                },
                                onSignUpClick = { 
                                    isLoggedIn = true
                                    currentScreen = "home"
                                },
                                onForgotPasswordClick = { /* TODO: Handle forgot password */ }
                            )
                        }
                        "home" -> {
                            // Show HomeScreen when logged in
                            HomeScreen(
                                username = "Ocean Lover",
                                onNavigateToEvents = { currentScreen = "events" },
                                onNavigateToProfile = { currentScreen = "profile" },
                                onNavigateToPollution = { currentScreen = "pollution" },
                                onNavigateToWasteTracking = { currentScreen = "waste_tracking" }
                            )
                        }
                        "pollution" -> {
                            // Show PollutionAwarenessScreen
                            PollutionAwarenessScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }
                        "events" -> {
                            // Show CleanupEventScreen
                            CleanupEventScreen(
                                onNavigateBack = { currentScreen = "home" },
                                onCreateEvent = { /* TODO: Implement create event */ }
                            )
                        }
                        "waste_tracking" -> {
                            // Show WasteTrackingScreen
                            WasteTrackingScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }
                        "profile" -> {
                            // Show ProfileScreen
                            ProfileScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }
                        // Add other screens as needed
                    }
                }
            }
        }
    }
}