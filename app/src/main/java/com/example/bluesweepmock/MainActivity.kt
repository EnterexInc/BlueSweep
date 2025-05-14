package com.example.bluesweepmock

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.bluesweepmock.data.UserDataStore
import com.example.bluesweepmock.ui.screens.*
import com.example.bluesweepmock.ui.theme.BlueSweepMockTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        userDataStore = UserDataStore(this)

        setContent {
            BlueSweepMockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
                    var currentScreen by remember { mutableStateOf(if (isLoggedIn) "home" else "login") }
                    var resetEmailDialogVisible by remember { mutableStateOf(false) }
                    var resetEmail by remember { mutableStateOf("") }

                    // Collect user profile data
                    val userName by userDataStore.userNameFlow.collectAsState(initial = "")
                    val userBio by userDataStore.userBioFlow.collectAsState(initial = "")
                    val userLocation by userDataStore.userLocationFlow.collectAsState(initial = "")
                    val userUsername by userDataStore.userUsernameFlow.collectAsState(initial = "")

                    // Get the current user's email
                    val userEmail = auth.currentUser?.email ?: ""

                    when (currentScreen) {
                        "login" -> {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    if (email.isBlank() || password.isBlank()) {
                                        Toast.makeText(
                                            this,
                                            "Email and password cannot be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@LoginScreen
                                    }
                                    
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(this) { task ->
                                            if (task.isSuccessful) {
                                                isLoggedIn = true
                                                currentScreen = "home"
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Login failed: ${task.exception?.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                },
                                onSignUpClick = { email, password ->
                                    if (email.isBlank() || password.isBlank()) {
                                        Toast.makeText(
                                            this,
                                            "Email and password cannot be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@LoginScreen
                                    }
                                    
                                    if (password.length < 6) {
                                        Toast.makeText(
                                            this,
                                            "Password must be at least 6 characters long",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@LoginScreen
                                    }
                                    
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(this) { task ->
                                            if (task.isSuccessful) {
                                                isLoggedIn = true
                                                currentScreen = "home"
                                                
                                                // Send email verification
                                                auth.currentUser?.sendEmailVerification()
                                                    ?.addOnCompleteListener { verificationTask ->
                                                        if (verificationTask.isSuccessful) {
                                                            Toast.makeText(
                                                                this,
                                                                "Verification email sent",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Sign-up failed: ${task.exception?.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                },
                                onForgotPasswordClick = {
                                    resetEmailDialogVisible = true
                                }
                            )
                            
                            // Password Reset Dialog
                            if (resetEmailDialogVisible) {
                                ResetPasswordDialog(
                                    email = resetEmail,
                                    onEmailChange = { resetEmail = it },
                                    onDismiss = { resetEmailDialogVisible = false },
                                    onReset = {
                                        if (resetEmail.isBlank()) {
                                            Toast.makeText(
                                                this,
                                                "Email cannot be empty",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@ResetPasswordDialog
                                        }
                                        
                                        auth.sendPasswordResetEmail(resetEmail)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        this,
                                                        "Password reset email sent",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    resetEmailDialogVisible = false
                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "Failed to send reset email: ${task.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    }
                                )
                            }
                        }

                        "home" -> {
                            HomeScreen(
                                username = userEmail,
                                onNavigateToEvents = { currentScreen = "events" },
                                onNavigateToProfile = { currentScreen = "profile" },
                                onNavigateToPollution = { currentScreen = "pollution" },
                                onNavigateToWasteTracking = { currentScreen = "waste_tracking" }
                            )
                        }

                        "pollution" -> {
                            PollutionAwarenessScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }

                        "events" -> {
                            CleanupEventScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }

                        "waste_tracking" -> {
                            WasteTrackingScreen(
                                onNavigateBack = { currentScreen = "home" }
                            )
                        }

                        "profile" -> {
                            ProfileScreen(
                                email = userEmail,
                                userName = userName,
                                userBio = userBio,
                                userLocation = userLocation,
                                userUsername = if (userUsername.isBlank()) "@${userEmail.substringBefore("@")}" else userUsername,
                                onNavigateBack = { currentScreen = "home" },
                                onUpdateProfile = { name, bio, location, username ->
                                    lifecycleScope.launch {
                                        userDataStore.updateUserProfile(name, bio, location, username)
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Profile updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onLogout = {
                                    auth.signOut()
                                    isLoggedIn = false
                                    currentScreen = "login"
                                    Toast.makeText(
                                        this,
                                        "Logged out successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
