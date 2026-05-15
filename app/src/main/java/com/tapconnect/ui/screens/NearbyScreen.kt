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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

data class NearbyUser(
    val name: String,
    val role: String,
    val org: String,
    val sharedInterests: Int,
    val angleDeg: Float,
    val radiusFraction: Float
)

val mockNearbyUsers = listOf(
    NearbyUser("Sarah Jenkins", "Founder", "AIStart", 2, 45f, 0.38f),
    NearbyUser("David Chen",    "UX Researcher", "BlueHorizon", 2, 160f, 0.62f),
    NearbyUser("Elena Rodriguez","VC", "BlueHorizon", 1, 260f, 0.5f),
)

@Composable
fun NearbyScreen() {
    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {

        // ── App bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().background(CardBg)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Nearby", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextH)
            Box(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background(AccentLight).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("${mockNearbyUsers.size} found", fontSize = 13.sp,
                    color = AccentIndigo, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // ── Radar Globe ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(CardBg),
                contentAlignment = Alignment.Center
            ) {
                NearbyRadarCanvas(users = mockNearbyUsers)
            }

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Divider)

            // ── User list ────────────────────────────────────────
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nearby", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextH)
                Text("${mockNearbyUsers.size} found", fontSize = 13.sp, color = TextSub)
            }
            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                mockNearbyUsers.forEachIndexed { index, user ->
                    NearbyUserRow(user = user)
                    if (index < mockNearbyUsers.lastIndex) {
                        HorizontalDivider(
                            color = Divider,
                            modifier = Modifier.padding(start = 74.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(90.dp))
        }
    }
}

@Composable
fun NearbyRadarCanvas(users: List<NearbyUser>) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val p1 by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2500, easing = LinearEasing)), label = "p1")
    val p2 by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2500, delayMillis = 833, easing = LinearEasing)), label = "p2")
    val p3 by infiniteTransition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(2500, delayMillis = 1666, easing = LinearEasing)), label = "p3")

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        val maxRDp = minOf(maxWidth, maxHeight) / 2f

        // Animated ripple rings
        Canvas(modifier = Modifier.matchParentSize()) {
            val c = center
            val maxR = maxRDp.toPx()
            listOf(p1, p2, p3).forEach { p ->
                drawCircle(
                    color = AccentIndigo.copy(alpha = (1f - p) * 0.12f),
                    radius = maxR * p,
                    center = c,
                    style = Stroke(1.5.dp.toPx())
                )
            }
            // Static inner circle marks
            listOf(0.33f, 0.66f, 1f).forEach { r ->
                drawCircle(
                    color = AccentIndigo.copy(alpha = 0.06f),
                    radius = maxR * r,
                    center = c,
                    style = Stroke(1.dp.toPx())
                )
            }
        }

        // Center "You" orb
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(AccentIndigo),
            contentAlignment = Alignment.Center
        ) {
            Text("You", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        // Nearby user avatars plotted on radar
        users.forEach { user ->
            val angleRad = Math.toRadians(user.angleDeg.toDouble())
            val radiusPx = maxRDp.value * user.radiusFraction
            val offsetX = (radiusPx * cos(angleRad)).dp
            val offsetY = (radiusPx * sin(angleRad)).dp

            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY)
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(2.dp, CardBg, CircleShape)
                    .background(AccentLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    user.name.take(1),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentIndigo
                )
            }
        }
    }
}

@Composable
fun NearbyUserRow(user: NearbyUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(AccentLight),
                contentAlignment = Alignment.Center
            ) {
                Text(user.name.take(1), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(user.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextH)
                Text("${user.role} @ ${user.org}", fontSize = 13.sp, color = TextSub)
                Spacer(Modifier.height(4.dp))
                // Shared interests chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        "${user.sharedInterests} Shared Interest${if (user.sharedInterests > 1) "s" else ""}",
                        fontSize = 11.sp,
                        color = AccentIndigo,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Connect icon
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(AccentLight),
            contentAlignment = Alignment.Center
        ) {
            Text("🔗", fontSize = 14.sp)
        }
    }
}
