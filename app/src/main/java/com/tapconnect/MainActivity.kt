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
import com.tapconnect.ui.screens.IcebreakerSuccessScreen
import com.tapconnect.ui.screens.NetworkingScreen
import com.tapconnect.ui.screens.ProfileSetupScreen
import com.tapconnect.ui.screens.SplashScreen
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

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        // 1. Splash Screen
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("profile_setup") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Profile Setup
                        composable("profile_setup") {
                            ProfileSetupScreen(
                                onSetupComplete = {
                                    navController.navigate("networking") {
                                        popUpTo("profile_setup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 3. Main Networking Radar Screen
                        composable("networking") {
                            NetworkingScreen(
                                onConnectionSuccess = { connectedName, icebreaker ->
                                    // URL encode the strings to pass as nav args
                                    val encodedName = java.net.URLEncoder.encode(connectedName, "UTF-8")
                                    val encodedIcebreaker = java.net.URLEncoder.encode(icebreaker, "UTF-8")
                                    navController.navigate("icebreaker_success/$encodedName/$encodedIcebreaker")
                                }
                            )
                        }

                        // 4. Icebreaker Success Screen
                        composable("icebreaker_success/{name}/{icebreaker}") { backStackEntry ->
                            val name = java.net.URLDecoder.decode(
                                backStackEntry.arguments?.getString("name") ?: "Someone", "UTF-8"
                            )
                            val icebreaker = java.net.URLDecoder.decode(
                                backStackEntry.arguments?.getString("icebreaker") ?: "You're both great networkers!", "UTF-8"
                            )
                            IcebreakerSuccessScreen(
                                connectedUserName = name,
                                icebreakerText = icebreaker,
                                onViewProfile = {
                                    // TODO: Navigate to user profile detail in Phase P2
                                    navController.popBackStack()
                                },
                                onBackToRadar = {
                                    navController.navigate("networking") {
                                        popUpTo("networking") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

