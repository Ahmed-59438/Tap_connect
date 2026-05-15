package com.tapconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.ui.screens.*
import com.tapconnect.ui.theme.TapConnectTheme
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

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
    var iceName by remember { mutableStateOf("") }
    var iceText by remember { mutableStateOf("") }

    // Observe networking state to colour radar icon
    val viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val isActive by viewModel.isNetworkingModeEnabled.collectAsState()

    if (showIcebreaker) {
        IcebreakerScreen(name = iceName, icebreaker = iceText,
            onViewProfile = { showIcebreaker = false; selectedTab = 2 },
            onBack = { showIcebreaker = false; selectedTab = 0 })
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {
            0 -> HomeScreen(onConnectionSuccess = { n, ib -> iceName = n; iceText = ib; showIcebreaker = true })
            1 -> NearbyScreen()
            2 -> ProfileScreen()
        }

        // ── Bottom nav ────────────────────────────────────────
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            HorizontalDivider(color = Color(0xFFE5E5EA), thickness = 0.5.dp)
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .navigationBarsPadding().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Radar/Home tab — icon turns green when active
                NavTab(
                    icon = Icons.Rounded.Sensors,
                    label = "Home",
                    selected = selectedTab == 0,
                    // Green when network is ON, indigo otherwise
                    activeColor = if (isActive) GreenDot else AccentIndigo,
                    onClick = { selectedTab = 0 }
                )
                NavTab(
                    icon = Icons.Rounded.PeopleAlt,
                    label = "Discover",
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavTab(
                    icon = Icons.Rounded.Person,
                    label = "Profile",
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    }
}

@Composable
fun NavTab(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    activeColor: Color = AccentIndigo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() }.padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) activeColor else Color(0xFFAEAEB2),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            label, fontSize = 10.sp,
            color = if (selected) activeColor else Color(0xFFAEAEB2),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun IcebreakerScreen(name: String, icebreaker: String, onViewProfile: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(AppBg).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = GreenDot, modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(16.dp))
        Text("Connected!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextH)
        Spacer(Modifier.height(6.dp))
        Text("You and $name are now connected.", fontSize = 15.sp, color = TextSub)
        Spacer(Modifier.height(28.dp))
        Column(
            modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(16.dp)).padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = AccentIndigo, modifier = Modifier.size(16.dp))
                Text("AI Icebreaker", fontSize = 13.sp, color = AccentIndigo, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            Text("\"$icebreaker\"", fontSize = 15.sp, color = TextH)
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = onViewProfile, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)) {
            Text("View Profile", fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 16.sp)
        }
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("← Back to Home", color = TextSub)
        }
    }
}
