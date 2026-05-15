package com.tapconnect.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

// ── Design tokens (light, clean, minimal) ──────────────────────────
val AppBg         = Color(0xFFF2F2F7)   // iOS-style light grey background
val CardBg        = Color(0xFFFFFFFF)   // Pure white cards
val AccentIndigo  = Color(0xFF5856D6)   // Indigo/purple accent
val AccentLight   = Color(0xFFEEEDFF)   // Light indigo tint
val GreenDot      = Color(0xFF34C759)   // iOS green
val TextH         = Color(0xFF1C1C1E)   // Primary headline
val TextSub       = Color(0xFF6E6E73)   // Secondary / muted
val Divider       = Color(0xFFE5E5EA)   // Subtle divider

@Composable
fun HomeScreen(
    onConnectionSuccess: (name: String, icebreaker: String) -> Unit = { _, _ -> },
    viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isActive by viewModel.isNetworkingModeEnabled.collectAsState()
    val nearbyUsers by viewModel.nearbyUsers.collectAsState()

    var showTapModal by remember { mutableStateOf(false) }
    var pendingRequest by remember { mutableStateOf<TapConnectionRequest?>(null) }

    // Session timer
    var sessionSec by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isActive) {
        if (isActive) { while (true) { kotlinx.coroutines.delay(1000); sessionSec++ } }
        else sessionSec = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── App bar ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "TapConnect",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextH
            )
            // Settings dot
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(AppBg)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text("···", color = TextSub, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Orb Card ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(1.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(CardBg)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status badge
            if (isActive) {
                ActiveStatusBadge()
            } else {
                InactiveStatusBadge()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // The orb
            OrbButton(isActive = isActive, onClick = { viewModel.toggleNetworkingMode(!isActive) })

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(Modifier.size(7.dp)) {
                    drawCircle(color = if (isActive) GreenDot else Color(0xFFAEAEB2))
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (isActive) "Visible via NFC & BLE" else "Tap orb to go discoverable",
                    fontSize = 13.sp,
                    color = TextSub
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Simulate Hardware Section ─────────────────────────────
        SectionHeader("Simulate Hardware")
        Text(
            "Since this is a dev build, use this to simulate a phone tap.",
            fontSize = 13.sp,
            color = TextSub,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                pendingRequest = TapConnectionRequest("1", "Ahmed Hassan", "Android Engineer", "Google")
                showTapModal = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)
        ) {
            Text("〜  Simulate NFC Tap", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── My Stats Section ──────────────────────────────────────
        SectionHeader("My Stats")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard2(value = "12", label = "Connections", modifier = Modifier.weight(1f))
            StatCard2(value = "4",  label = "Events",      modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Radio Status ──────────────────────────────────────────
        SectionHeader("Hardware Status")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(CardBg)
        ) {
            RadioRow(icon = "📶", title = "NFC", sub = "Tap-to-connect", ready = true)
            HorizontalDivider(color = Divider, modifier = Modifier.padding(start = 52.dp))
            RadioRow(icon = "🔵", title = "Bluetooth LE", sub = "Background discovery", ready = true)
        }

        Spacer(modifier = Modifier.height(90.dp))
    }

    // ── Tap modal ────────────────────────────────────────────────
    if (showTapModal && pendingRequest != null) {
        TapConnectModal(
            connectionRequest = pendingRequest!!,
            onConnect = {
                showTapModal = false
                onConnectionSuccess(
                    pendingRequest!!.name,
                    "You both love building for Android. Ask ${pendingRequest!!.name} about their favourite Compose pattern!"
                )
                pendingRequest = null
            },
            onDismiss = { showTapModal = false; pendingRequest = null }
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// Subcomponents
// ─────────────────────────────────────────────────────────────────

@Composable
private fun OrbButton(isActive: Boolean, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val ring1 by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2200, easing = LinearEasing)), label = "r1")
    val ring2 by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2200, delayMillis = 750, easing = LinearEasing)), label = "r2")

    Box(modifier = Modifier.size(160.dp), contentAlignment = Alignment.Center) {
        // Ripple rings when active
        if (isActive) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val maxR = size.minDimension / 2f
                listOf(ring1, ring2).forEach { p ->
                    drawCircle(
                        color = AccentIndigo.copy(alpha = (1f - p) * 0.18f),
                        radius = maxR * p,
                        style = Stroke(2.dp.toPx())
                    )
                }
            }
        }
        // Outer glow circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) AccentIndigo.copy(alpha = 0.10f)
                    else Color(0xFFF2F2F7)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner orb
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive)
                            Brush.radialGradient(listOf(Color(0xFF7C7AE8), AccentIndigo))
                        else
                            Brush.radialGradient(listOf(Color(0xFFBEBEC6), Color(0xFF8E8E93)))
                    )
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("((·))", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ActiveStatusBadge() {
    val inf = rememberInfiniteTransition(label = "badge_dot")
    val a by inf.animateFloat(0.5f, 1f,
        infiniteRepeatable(tween(800, easing = EaseInOut), RepeatMode.Reverse), label = "a")
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(AccentLight)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Canvas(Modifier.size(6.dp)) { drawCircle(GreenDot.copy(alpha = a)) }
            Text("ACTIVE & DISCOVERABLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentIndigo, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
private fun InactiveStatusBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFF2F2F7))
            .border(1.dp, Divider, RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text("INACTIVE · TAP TO ACTIVATE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSub)
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = TextH,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun StatCard2(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
        Text(label, fontSize = 13.sp, color = TextSub)
    }
}

@Composable
fun RadioRow(icon: String, title: String, sub: String, ready: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 20.sp)
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextH)
                Text(sub, fontSize = 12.sp, color = TextSub)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Canvas(Modifier.size(7.dp)) { drawCircle(if (ready) GreenDot else Color(0xFFFF3B30)) }
            Text(if (ready) "Ready" else "Unavailable", fontSize = 13.sp, color = if (ready) GreenDot else Color(0xFFFF3B30), fontWeight = FontWeight.Medium)
        }
    }
}
