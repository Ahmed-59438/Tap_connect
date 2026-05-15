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
import kotlin.math.cos
import kotlin.math.sin

data class NearbyUser(
    val name: String, val role: String, val org: String,
    val sharedInterests: Int, val angleDeg: Float, val radiusFraction: Float
)

val mockNearbyUsers = listOf(
    NearbyUser("Sarah Jenkins", "Founder",       "AIStart",      2, 50f,  0.38f),
    NearbyUser("David Chen",    "UX Researcher", "BlueHorizon",  2, 170f, 0.60f),
    NearbyUser("Elena Rodriguez","VC",           "BlueHorizon",  1, 270f, 0.48f),
)

@Composable
fun NearbyScreen() {
    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {

        // ── Top bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().background(CardBg)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Nearby", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextH)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Canvas(Modifier.size(7.dp)) { drawCircle(color = GreenDot) }
                Text("${mockNearbyUsers.size} found", fontSize = 13.sp, color = TextSub, fontWeight = FontWeight.Medium)
            }
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            // ── Radar ────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth().height(270.dp).background(CardBg),
                contentAlignment = Alignment.Center
            ) {
                NearbyRadar(users = mockNearbyUsers)
            }
            HorizontalDivider(color = Divider)

            // ── List ─────────────────────────────────────────────
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("People Nearby", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextH)
                Text("${mockNearbyUsers.size} found", fontSize = 13.sp, color = TextSub)
            }
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .shadow(1.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp)).background(CardBg)
            ) {
                mockNearbyUsers.forEachIndexed { i, user ->
                    NearbyRow(user)
                    if (i < mockNearbyUsers.lastIndex)
                        HorizontalDivider(color = Divider, modifier = Modifier.padding(start = 72.dp))
                }
            }
            Spacer(Modifier.height(90.dp))
        }
    }
}

@Composable
fun NearbyRadar(users: List<NearbyUser>) {
    val inf = rememberInfiniteTransition(label = "radar")
    val p1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2500, easing = LinearEasing)), label = "p1")
    val p2 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2500, delayMillis = 833, easing = LinearEasing)), label = "p2")
    val p3 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2500, delayMillis = 1666, easing = LinearEasing)), label = "p3")

    BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(270.dp), contentAlignment = Alignment.Center) {
        val maxRDp = minOf(maxWidth, maxHeight) / 2f

        Canvas(modifier = Modifier.matchParentSize()) {
            val c = center; val maxR = maxRDp.toPx()
            listOf(0.33f, 0.66f, 1f).forEach { r ->
                drawCircle(color = AccentIndigo.copy(alpha = 0.05f), radius = maxR * r, center = c, style = Stroke(1.dp.toPx()))
            }
            listOf(p1, p2, p3).forEach { p ->
                drawCircle(color = AccentIndigo.copy(alpha = (1f - p) * 0.12f), radius = maxR * p, center = c, style = Stroke(1.5.dp.toPx()))
            }
        }

        // Center orb with Sensors icon
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(AccentIndigo),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Sensors, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
        }

        // User avatars on radar
        users.forEach { user ->
            val rad = Math.toRadians(user.angleDeg.toDouble())
            val r = maxRDp.value * user.radiusFraction
            Box(
                modifier = Modifier
                    .offset(x = (r * cos(rad)).dp, y = (r * sin(rad)).dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .border(2.dp, CardBg, CircleShape)
                    .background(AccentLight),
                contentAlignment = Alignment.Center
            ) {
                Text(user.name.take(1), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
            }
        }
    }
}

@Composable
fun NearbyRow(user: NearbyUser) {
    Row(
        modifier = Modifier.fillMaxWidth().background(CardBg).clickable {}
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(AccentLight),
                contentAlignment = Alignment.Center
            ) {
                Text(user.name.take(1), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(user.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextH)
                Text("${user.role} @ ${user.org}", fontSize = 13.sp, color = TextSub)
                Spacer(Modifier.height(3.dp))
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(AccentLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("${user.sharedInterests} Shared Interest${if (user.sharedInterests > 1) "s" else ""}",
                        fontSize = 11.sp, color = AccentIndigo, fontWeight = FontWeight.Medium)
                }
            }
        }
        Icon(
            Icons.Rounded.PersonAdd, contentDescription = "Connect",
            tint = AccentIndigo, modifier = Modifier.size(22.dp)
        )
    }
}
