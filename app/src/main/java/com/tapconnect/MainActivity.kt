package com.tapconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tapconnect.ui.screens.*
import com.tapconnect.ui.theme.TapConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TapConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "splash") {

                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("main") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("main") {
                            MainScaffold(
                                onConnectionSuccess = { name, icebreaker ->
                                    val n = java.net.URLEncoder.encode(name, "UTF-8")
                                    val ic = java.net.URLEncoder.encode(icebreaker, "UTF-8")
                                    navController.navigate("icebreaker/$n/$ic")
                                }
                            )
                        }

                        composable("icebreaker/{name}/{icebreaker}") { back ->
                            val name = java.net.URLDecoder.decode(
                                back.arguments?.getString("name") ?: "Someone", "UTF-8"
                            )
                            val icebreaker = java.net.URLDecoder.decode(
                                back.arguments?.getString("icebreaker") ?: "Great to connect!", "UTF-8"
                            )
                            IcebreakerSuccessScreen(
                                connectedUserName = name,
                                icebreakerText = icebreaker,
                                onViewProfile = { navController.popBackStack() },
                                onBackToRadar = {
                                    navController.navigate("main") {
                                        popUpTo("main") { inclusive = true }
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

// ── Main Scaffold with bottom nav ────────────────────────────────

@Composable
fun MainScaffold(onConnectionSuccess: (String, String) -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem("🏠", "Home"),
        TabItem("📡", "Nearby"),
        TabItem("👤", "Profile")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Screen content
        when (selectedTab) {
            0 -> HomeScreen(onConnectionSuccess = onConnectionSuccess)
            1 -> NearbyScreen()
            2 -> ProfileScreen()
        }

        // Bottom nav bar pinned to bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Top border line
            Box(
                modifier = Modifier
                    .align(Alignment.Top)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color(0xFFE5E5EA))
            )
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTab == index
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = index }
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(tab.icon, fontSize = 22.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        tab.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) AccentIndigo else Color(0xFF8E8E93)
                    )
                }
            }
        }
    }
}

data class TabItem(val icon: String, val label: String)
