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
import androidx.compose.ui.geometry.Offset
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

// ── Shared design tokens ─────────────────────────────────────────
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
    var pending  by remember { mutableStateOf<TapConnectionRequest?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top bar ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("TapConnect", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextH)
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
            // Status label
            Text(
                text = if (isActive) "ONLINE" else "OFFLINE",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) AccentIndigo else TextSub,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(20.dp))

            // Power-button orb
            PowerOrb(isActive = isActive, onClick = { viewModel.toggleNetworkingMode(!isActive) })

            Spacer(Modifier.height(20.dp))

            // Caption
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔒", fontSize = 12.sp)
                Spacer(Modifier.width(4.dp))
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
            "Since this is a browser demo, use this to simulate a phone tap.",
            fontSize = 13.sp,
            color = TextSub,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                pending = TapConnectionRequest("1", "Ahmed Hassan", "Android Engineer", "Google")
                showModal = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF48484A))
        ) {
            Text("〜  Simulate NFC Tap", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
        }

        Spacer(Modifier.height(28.dp))

        // ── My Stats ─────────────────────────────────────────────
        SectionLabel("My Stats")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(value = "12", label = "Connections", modifier = Modifier.weight(1f))
            StatCard(value = "4",  label = "Events",      modifier = Modifier.weight(1f))
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
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = EaseInOut), RepeatMode.Reverse),
        label = "pulse"
    )
    val ringProgress by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2200, easing = LinearEasing)), label = "ring")

    Box(
        modifier = Modifier.size(130.dp),
        contentAlignment = Alignment.Center
    ) {
        // Expanding ring when active
        if (isActive) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = AccentIndigo.copy(alpha = (1f - ringProgress) * 0.15f),
                    radius = size.minDimension / 2f * ringProgress,
                    style = Stroke(2.dp.toPx())
                )
            }
        }

        // Outer ring
        Box(
            modifier = Modifier
                .size((if (isActive) 110.dp * pulse else 110.dp))
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    color = if (isActive) AccentIndigo.copy(alpha = 0.35f) else Color(0xFFD1D1D6),
                    shape = CircleShape
                )
                .background(if (isActive) AccentLight else Color(0xFFF2F2F7))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Power icon (⏻)
            Text(
                text = "⏻",
                fontSize = 36.sp,
                color = if (isActive) AccentIndigo else Color(0xFFAEAEB2)
            )
        }
    }
}

// ── Shared small components ──────────────────────────────────────

@Composable
fun SectionLabel(title: String) {
    Text(
        title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = TextH,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )
    Spacer(Modifier.height(6.dp))
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
        Text(label, fontSize = 13.sp, color = TextSub)
    }
}
