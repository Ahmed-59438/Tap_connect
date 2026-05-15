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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

// ---------- Minimal Professional Palette ----------
private val BgDark = Color(0xFF0C0C0E)         // Near-black background
private val SurfaceDark = Color(0xFF18181B)    // Card / surface
private val SurfaceMid = Color(0xFF27272A)     // Elevated card
private val BorderSubtle = Color(0xFF3F3F46)   // Subtle border
private val AccentGreen = Color(0xFF22C55E)    // Active / Ready indicator
private val AccentBlue = Color(0xFF3B82F6)     // Primary accent (clean blue)
private val TextPrimary = Color(0xFFF4F4F5)    // Headline text
private val TextSecondary = Color(0xFF71717A)  // Muted body text
private val TextMuted = Color(0xFF52525B)      // Very muted (labels)

@Composable
fun NetworkingScreen(
    onConnectionSuccess: (connectedName: String, icebreaker: String) -> Unit = { _, _ -> },
    viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isNetworkingOn by viewModel.isNetworkingModeEnabled.collectAsState()
    val nearbyUsers by viewModel.nearbyUsers.collectAsState()

    var showTapModal by remember { mutableStateOf(false) }
    var pendingConnectionRequest by remember { mutableStateOf<TapConnectionRequest?>(null) }

    // Session timer (mock)
    var sessionSeconds by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isNetworkingOn) {
        if (isNetworkingOn) {
            while (true) {
                kotlinx.coroutines.delay(1000)
                sessionSeconds++
            }
        } else {
            sessionSeconds = 0L
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Networking Mode",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = if (isNetworkingOn) "You are discoverable nearby"
                           else "You are invisible",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                )
            }
            // Refresh / Settings icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceDark)
                    .border(1.dp, BorderSubtle, CircleShape)
                    .clickable { /* TODO: open settings */ },
                contentAlignment = Alignment.Center
            ) {
                Text("⟳", color = TextSecondary, fontSize = 18.sp)
            }
        }

        // ── Central ON/OFF Radar Orb ──────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Pulsing rings behind the orb
            RadarRingsCanvas(isActive = isNetworkingOn)

            // The central orb
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        if (isNetworkingOn)
                            Brush.radialGradient(listOf(Color(0xFF1A3A5C), Color(0xFF0F2035)))
                        else
                            Brush.radialGradient(listOf(SurfaceMid, SurfaceDark))
                    )
                    .border(1.dp, if (isNetworkingOn) AccentBlue.copy(alpha = 0.4f) else BorderSubtle, CircleShape)
                    .clickable { viewModel.toggleNetworkingMode(!isNetworkingOn) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Radio waves icon (drawn with text char for now)
                    Text(
                        text = if (isNetworkingOn) "((·))" else "((·))",
                        fontSize = 22.sp,
                        color = if (isNetworkingOn) AccentBlue else TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isNetworkingOn) "ON" else "OFF",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isNetworkingOn) TextPrimary else TextMuted
                    )
                }
            }
        }

        // ── Status Badge ───────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isNetworkingOn) {
                ActiveBadge("ACTIVE & DISCOVERABLE")
            } else {
                InactiveBadge("INVISIBLE · TAP ORB TO ACTIVATE")
            }
        }

        // Helper text
        Text(
            text = if (isNetworkingOn) "Tap to go invisible. Your profile is being shared with nearby users."
                   else "Tap the orb to start sharing your profile.",
            style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondary, fontSize = 13.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Nearby People Card ─────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                .clickable {
                    if (isNetworkingOn) {
                        pendingConnectionRequest = TapConnectionRequest(
                            userId = "test_user_1",
                            name = "Sarah Ali",
                            role = "Android Engineer",
                            organization = "Google"
                        )
                        showTapModal = true
                    }
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceMid),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👥", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isNetworkingOn) "${nearbyUsers.size} people nearby"
                               else "Activate to discover people",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isNetworkingOn) "Tap to see who's around" else "Go live to find connections",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            Text(text = "›", fontSize = 22.sp, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Stats Row ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                label = "SESSION TIME",
                icon = "🕐",
                value = formatSeconds(sessionSeconds),
                subtitle = if (isNetworkingOn) "active networking" else "not active",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "CONNECTIONS",
                icon = "⚡",
                value = "3",
                subtitle = "this session",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Radio Status Card ──────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📡", fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Radio Status", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("Hardware availability for discovery", fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = BorderSubtle, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(14.dp))

            RadioStatusRow(icon = "📶", name = "NFC", subtitle = "Tap-to-connect", isReady = true)
            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = BorderSubtle, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(14.dp))
            RadioStatusRow(icon = "🔵", name = "Bluetooth LE", subtitle = "Background discovery", isReady = true)
        }

        Spacer(modifier = Modifier.height(80.dp)) // Bottom nav clearance
    }

    // ── Bottom Navigation ──────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        BottomNavBar(activeTab = 0)
    }

    // ── Tap Modal ──────────────────────────────────────────────
    if (showTapModal && pendingConnectionRequest != null) {
        TapConnectModal(
            connectionRequest = pendingConnectionRequest!!,
            onConnect = {
                showTapModal = false
                onConnectionSuccess(
                    pendingConnectionRequest!!.name,
                    "You both build in Kotlin. Ask ${pendingConnectionRequest!!.name} about her experience with Compose animations!"
                )
                pendingConnectionRequest = null
            },
            onDismiss = {
                showTapModal = false
                pendingConnectionRequest = null
            }
        )
    }
}

// ── Subcomponents ────────────────────────────────────────────────

@Composable
fun ActiveBadge(text: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot_pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = EaseInOut), RepeatMode.Reverse),
        label = "dot"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(AccentGreen.copy(alpha = 0.12f))
            .border(1.dp, AccentGreen.copy(alpha = 0.3f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Canvas(modifier = Modifier.size(6.dp)) {
                drawCircle(color = AccentGreen.copy(alpha = dotAlpha))
            }
            Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = AccentGreen)
        }
    }
}

@Composable
fun InactiveBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(SurfaceMid)
            .border(1.dp, BorderSubtle, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
    }
}

@Composable
fun RadarRingsCanvas(isActive: Boolean) {
    if (!isActive) {
        // Static dim rings when off
        Canvas(modifier = Modifier.size(260.dp)) {
            val c = center
            listOf(0.35f, 0.6f, 0.85f).forEach { r ->
                drawCircle(color = Color(0xFF27272A), radius = size.minDimension / 2f * r, center = c, style = Stroke(1.dp.toPx()))
            }
        }
        return
    }
    val infiniteTransition = rememberInfiniteTransition(label = "rings")
    val p1 by infiniteTransition.animateFloat(0f, 1f, infiniteRepeatable(tween(2800, easing = LinearEasing)), label = "p1")
    val p2 by infiniteTransition.animateFloat(0f, 1f, infiniteRepeatable(tween(2800, delayMillis = 900, easing = LinearEasing)), label = "p2")
    val p3 by infiniteTransition.animateFloat(0f, 1f, infiniteRepeatable(tween(2800, delayMillis = 1800, easing = LinearEasing)), label = "p3")

    Canvas(modifier = Modifier.size(260.dp)) {
        val c = center; val maxR = size.minDimension / 2f
        listOf(p1, p2, p3).forEach { p ->
            drawCircle(
                color = AccentBlue.copy(alpha = (1f - p) * 0.25f),
                radius = maxR * p,
                center = c,
                style = Stroke(1.5.dp.toPx())
            )
        }
    }
}

@Composable
fun StatCard(label: String, icon: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(icon, fontSize = 13.sp)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextMuted, letterSpacing = 0.8.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(subtitle, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
fun RadioStatusRow(icon: String, name: String, subtitle: String, isReady: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(icon, fontSize = 18.sp)
            Column {
                Text(name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Text(subtitle, fontSize = 12.sp, color = TextSecondary)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Canvas(modifier = Modifier.size(7.dp)) {
                drawCircle(color = if (isReady) AccentGreen else Color(0xFFEF4444))
            }
            Text(
                text = if (isReady) "Ready" else "Unavailable",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isReady) AccentGreen else Color(0xFFEF4444)
            )
        }
    }
}

@Composable
fun BottomNavBar(activeTab: Int) {
    val tabs = listOf("((·))" to "Radar", "👥" to "Nearby", "🤝" to "Connected", "👤" to "Profile")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgDark)
            .border(width = 0.5.dp, color = BorderSubtle, shape = RoundedCornerShape(0.dp))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, (icon, label) ->
            val isActive = index == activeTab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { /* navigate */ }
            ) {
                Text(icon, fontSize = 20.sp, color = if (isActive) AccentBlue else TextSecondary)
                Text(
                    label, fontSize = 10.sp,
                    color = if (isActive) AccentBlue else TextSecondary,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                )
                if (isActive) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Canvas(modifier = Modifier.size(4.dp)) {
                        drawCircle(color = AccentBlue)
                    }
                }
            }
        }
    }
}

private fun formatSeconds(s: Long): String {
    val m = s / 60; val sec = s % 60
    return "${m}m ${sec.toString().padStart(2, '0')}s"
}
