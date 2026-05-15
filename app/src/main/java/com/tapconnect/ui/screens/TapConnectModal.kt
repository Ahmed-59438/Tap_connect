package com.tapconnect.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tapconnect.ui.theme.Primary
import com.tapconnect.ui.theme.Secondary
import com.tapconnect.ui.theme.Surface
import kotlinx.coroutines.delay

data class TapConnectionRequest(
    val userId: String,
    val name: String,
    val role: String,
    val organization: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TapConnectModal(
    connectionRequest: TapConnectionRequest,
    onConnect: () -> Unit,
    onDismiss: () -> Unit
) {
    // 8-second auto-dismiss countdown
    var timeoutProgress by remember { mutableFloatStateOf(1f) }
    val animatedTimeout by animateFloatAsState(
        targetValue = timeoutProgress,
        animationSpec = tween(8000, easing = LinearEasing),
        label = "timeout_progress",
        finishedListener = { onDismiss() }
    )

    // Pulse animation for the avatars
    val infiniteTransition = rememberInfiniteTransition(label = "modal_pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    LaunchedEffect(Unit) {
        timeoutProgress = 0f
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Timeout Progress Bar (at top, under handle)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedTimeout)
                        .clip(CircleShape)
                        .background(Primary.copy(alpha = 0.5f))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Overlapping Avatars: You ↔ Them
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // "You" avatar (left)
                AvatarBubble(
                    label = "You",
                    color = Primary,
                    glowAlpha = glowAlpha,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                // Connection spark icon (center)
                Text(
                    text = "↔",
                    color = Secondary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
                // "Them" avatar (right)
                AvatarBubble(
                    label = connectionRequest.name.take(1).uppercase(),
                    color = Secondary,
                    glowAlpha = glowAlpha,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Connection info
            Text(
                text = "Connect with ${connectionRequest.name}?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${connectionRequest.role} at ${connectionRequest.organization}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Connect Button
            Button(
                onClick = onConnect,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "💚  Connect",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dismiss
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✕  Dismiss",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun AvatarBubble(
    label: String,
    color: Color,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(72.dp), contentAlignment = Alignment.Center) {
        // Glow ring
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = color.copy(alpha = glowAlpha * 0.4f),
                radius = size.minDimension / 2f,
                style = Stroke(width = 8f)
            )
        }
        // Avatar circle
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(color, color.copy(alpha = 0.7f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
