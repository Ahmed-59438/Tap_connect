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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

// ── Design tokens ────────────────────────────────────────────────
val AppBg        = Color(0xFFF2F2F7)
val CardBg       = Color(0xFFFFFFFF)
val AccentIndigo = Color(0xFF5856D6)
val AccentLight  = Color(0xFFEEEDFF)
val GreenDot     = Color(0xFF34C759)
val TextH        = Color(0xFF1C1C1E)
val TextSub      = Color(0xFF6E6E73)
val Divider      = Color(0xFFE5E5EA)

@Composable
fun HomeScreen(
    onConnectionSuccess: (String, String) -> Unit = { _, _ -> },
    viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isActive by viewModel.isNetworkingModeEnabled.collectAsState()
    var showModal by remember { mutableStateOf(false) }
    var pending   by remember { mutableStateOf<TapConnectionRequest?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().background(CardBg)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("TapConnect", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextH)
            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = TextSub, modifier = Modifier.size(22.dp))
        }

        Spacer(Modifier.height(16.dp))

        // ── Orb Card ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(2.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(CardBg)
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status badge with dot
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Canvas(Modifier.size(7.dp)) {
                    drawCircle(color = if (isActive) GreenDot else Color(0xFFAEAEB2))
                }
                Text(
                    text = if (isActive) "ONLINE" else "OFFLINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive) GreenDot else TextSub,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(22.dp))

            // Power orb
            PowerOrb(isActive = isActive, onClick = { viewModel.toggleNetworkingMode(!isActive) })

            Spacer(Modifier.height(22.dp))

            // Caption
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(
                    imageVector = if (isActive) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                    contentDescription = null,
                    tint = TextSub,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (isActive) "You are live. Tap to go hidden." else "You are hidden. Tap to go live.",
                    fontSize = 13.sp,
                    color = TextSub
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Simulate Hardware ────────────────────────────────────
        SectionLabel("Simulate Hardware")
        Text(
            "Since this is a dev build, use this to simulate a phone tap.",
            fontSize = 13.sp, color = TextSub,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                pending = TapConnectionRequest("1", "Ahmed Hassan", "Android Engineer", "Google")
                showModal = true
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3C))
        ) {
            Icon(Icons.Rounded.Sensors, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Simulate NFC Tap", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
        }

        Spacer(Modifier.height(28.dp))

        // ── My Stats ─────────────────────────────────────────────
        SectionLabel("My Stats")
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard2("12", "Connections", Modifier.weight(1f))
            StatCard2("4",  "Events",      Modifier.weight(1f))
        }

        Spacer(Modifier.height(28.dp))

        // ── Hardware Status ──────────────────────────────────────
        SectionLabel("Hardware Status")
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp)).background(CardBg)
        ) {
            HWRow(icon = { Icon(Icons.Rounded.Nfc, contentDescription = null, tint = AccentIndigo, modifier = Modifier.size(20.dp)) },
                title = "NFC", sub = "Tap-to-connect", ready = true)
            HorizontalDivider(color = Divider, modifier = Modifier.padding(start = 52.dp))
            HWRow(icon = { Icon(Icons.Rounded.Bluetooth, contentDescription = null, tint = Color(0xFF007AFF), modifier = Modifier.size(20.dp)) },
                title = "Bluetooth LE", sub = "Background discovery", ready = true)
        }

        Spacer(Modifier.height(90.dp))
    }

    if (showModal && pending != null) {
        TapConnectModal(
            connectionRequest = pending!!,
            onConnect = {
                showModal = false
                onConnectionSuccess(pending!!.name, "You both love Android. Ask ${pending!!.name} about their favourite Compose tip!")
                pending = null
            },
            onDismiss = { showModal = false; pending = null }
        )
    }
}

// ── Power Orb ────────────────────────────────────────────────────

@Composable
fun PowerOrb(isActive: Boolean, onClick: () -> Unit) {
    val inf = rememberInfiniteTransition(label = "orb")
    val ring by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2000, easing = LinearEasing)), label = "ring")
    val pulse by inf.animateFloat(1f, 1.06f,
        infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse), label = "pulse")

    Box(modifier = Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        if (isActive) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = AccentIndigo.copy(alpha = (1f - ring) * 0.14f),
                    radius = size.minDimension / 2f * ring,
                    style = Stroke(2.dp.toPx())
                )
            }
        }
        Box(
            modifier = Modifier
                .size(if (isActive) 108.dp * pulse else 108.dp)
                .clip(CircleShape)
                .border(1.5.dp, if (isActive) AccentIndigo.copy(0.4f) else Color(0xFFD1D1D6), CircleShape)
                .background(if (isActive) AccentLight else Color(0xFFF2F2F7))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = "Toggle",
                tint = if (isActive) AccentIndigo else Color(0xFFAEAEB2),
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

// ── Shared small components ──────────────────────────────────────

@Composable
fun SectionLabel(title: String) {
    Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextH,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
    Spacer(Modifier.height(6.dp))
}

@Composable
fun StatCard2(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.shadow(1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp)).background(CardBg).padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
        Text(label, fontSize = 13.sp, color = TextSub)
    }
}

@Composable
fun HWRow(icon: @Composable () -> Unit, title: String, sub: String, ready: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            icon()
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextH)
                Text(sub, fontSize = 12.sp, color = TextSub)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Canvas(Modifier.size(7.dp)) { drawCircle(color = if (ready) GreenDot else Color(0xFFFF3B30)) }
            Text(if (ready) "Ready" else "Unavailable", fontSize = 13.sp,
                color = if (ready) GreenDot else Color(0xFFFF3B30), fontWeight = FontWeight.Medium)
        }
    }
}
