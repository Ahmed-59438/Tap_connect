package com.tapconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tapconnect.ui.screens.NetworkingScreen
import com.tapconnect.ui.screens.ProfileSetupScreen
import com.tapconnect.ui.theme.TapConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TapConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "profile_setup") {
                        composable("profile_setup") {
                            ProfileSetupScreen(
                                onSetupComplete = {
                                    navController.navigate("networking") {
                                        popUpTo("profile_setup") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("networking") {
                            NetworkingScreen()
                        }
                    }
                }
            }
        }
    }
}
