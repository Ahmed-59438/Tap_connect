package com.tapconnect.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tapconnect.ui.theme.Background
import com.tapconnect.ui.theme.Primary
import com.tapconnect.ui.theme.Secondary
import com.tapconnect.ui.theme.Surface

@Composable
fun IcebreakerSuccessScreen(
    connectedUserName: String,
    icebreakerText: String,
    onViewProfile: () -> Unit,
    onBackToRadar: () -> Unit
) {
    // Confetti / particle burst animation (scale-in)
    var visible by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 200),
        label = "content_alpha"
    )
    LaunchedEffect(Unit) { visible = true }

    // Animated glow border for AI card
    val infiniteTransition = rememberInfiniteTransition(label = "glow_border")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Background, Color(0xFF0A1628))
                )
            )
    ) {
        // Confetti particles (canvas drawn)
        ConfettiCanvas()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon with animated scale
            Box(
                modifier = Modifier
                    .size(100.dp * cardScale)
                    .clip(CircleShape)
                    .background(Secondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp * cardScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Secondary, Color(0xFF059669))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✓", color = Color.White, fontSize = (32 * cardScale).sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Connected!",
                style = MaterialTheme.typography.displayLarge,
                color = Secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer(alpha = contentAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You and $connectedUserName are now connected.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer(alpha = contentAlpha)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // AI Icebreaker Card with glowing border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Primary.copy(alpha = glowAlpha),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface.copy(alpha = 0.8f))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🤖", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Icebreaker",
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"$icebreakerText\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // View Profile Button
            Button(
                onClick = onViewProfile,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "View Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Back to Radar
            TextButton(
                onClick = onBackToRadar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "← Back to Radar",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ConfettiCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val offset by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_offset"
    )

    val confettiColors = listOf(Primary, Secondary, Color(0xFFF59E0B), Color(0xFFEC4899), Color(0xFF06B6D4))

    Canvas(modifier = Modifier.fillMaxSize()) {
        val positions = listOf(
            Pair(80f, offset % size.height),
            Pair(160f, (offset + 120f) % size.height),
            Pair(250f, (offset + 60f) % size.height),
            Pair(320f, (offset + 200f) % size.height),
            Pair(180f, (offset + 300f) % size.height),
        )
        positions.forEachIndexed { i, (x, y) ->
            drawCircle(
                color = confettiColors[i % confettiColors.size].copy(alpha = 0.6f),
                radius = 6f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }
    }
}
