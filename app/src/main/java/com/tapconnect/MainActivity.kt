package com.tapconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tapconnect.ui.screens.*
import com.tapconnect.ui.theme.TapConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TapConnectTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScaffold()
                }
            }
        }
    }
}

@Composable
fun MainScaffold() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showIcebreaker by remember { mutableStateOf(false) }
    var icebreakerName by remember { mutableStateOf("") }
    var icebreakerText by remember { mutableStateOf("") }

    if (showIcebreaker) {
        IcebreakerSuccessScreen(
            connectedUserName = icebreakerName,
            icebreakerText = icebreakerText,
            onViewProfile = { showIcebreaker = false; selectedTab = 2 },
            onBackToRadar = { showIcebreaker = false; selectedTab = 0 }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {
            0 -> HomeScreen(onConnectionSuccess = { name, ib -> icebreakerName = name; icebreakerText = ib; showIcebreaker = true })
            1 -> NearbyScreen()
            2 -> ProfileScreen()
        }

        // ── Bottom nav ────────────────────────────────────────
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalDivider(color = Color(0xFFE5E5EA), thickness = 0.5.dp)
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .navigationBarsPadding().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf(Triple("🏠", "Home", 0), Triple("📡", "Discover", 1), Triple("👤", "Profile", 2))
                    .forEach { (icon, label, idx) ->
                        val active = selectedTab == idx
                        Column(
                            modifier = Modifier.clickable { selectedTab = idx }.padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(icon, fontSize = 22.sp)
                            Text(
                                label, fontSize = 10.sp,
                                color = if (active) AccentIndigo else Color(0xFF8E8E93),
                                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
            }
        }
    }
}

// ── Icebreaker inline screen ─────────────────────────────────────
@Composable
fun IcebreakerSuccessScreen(
    connectedUserName: String,
    icebreakerText: String,
    onViewProfile: () -> Unit,
    onBackToRadar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(AppBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✓", fontSize = 64.sp, color = GreenDot)
        Spacer(Modifier.height(16.dp))
        Text("Connected!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextH)
        Spacer(Modifier.height(8.dp))
        Text("You and $connectedUserName are now connected.", fontSize = 15.sp, color = TextSub)
        Spacer(Modifier.height(28.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(CardBg, androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text("🤖  AI Icebreaker", fontSize = 13.sp, color = AccentIndigo, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text("\"$icebreakerText\"", fontSize = 15.sp, color = TextH)
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = onViewProfile, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)) {
            Text("View Profile", fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 16.sp)
        }
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onBackToRadar, modifier = Modifier.fillMaxWidth()) {
            Text("← Back to Home", color = TextSub)
        }
    }
}
