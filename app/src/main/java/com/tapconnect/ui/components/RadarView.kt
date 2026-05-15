package com.tapconnect.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

data class RadarUser(
    val id: String,
    val name: String,
    val distanceRadius: Float, // 0.0 to 1.0 (center to edge)
    val angleDegrees: Float // 0 to 360
)

@Composable
fun RadarView(
    isScanning: Boolean,
    users: List<RadarUser>,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        val maxRadiusDp = maxWidth / 2

        if (isScanning) {
            RadarRipples(maxRadiusDp)
        } else {
            StaticRipples(maxRadiusDp)
        }

        // Center Device Node
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(4.dp, Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "You",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        // Render Discovered Users
        users.forEach { user ->
            val angleRad = Math.toRadians(user.angleDegrees.toDouble())
            // Calculate absolute radius in dp based on the distance multiplier
            val absoluteRadius = maxRadiusDp.value * user.distanceRadius
            
            val offsetX = (absoluteRadius * cos(angleRad)).dp
            val offsetY = (absoluteRadius * sin(angleRad)).dp

            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StaticRipples(maxRadius: Dp) {
    val rippleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = center
        val maxRad = maxRadius.toPx()
        
        drawCircle(color = rippleColor, radius = maxRad * 0.3f, center = center, style = Stroke(width = 2f))
        drawCircle(color = rippleColor, radius = maxRad * 0.6f, center = center, style = Stroke(width = 2f))
        drawCircle(color = rippleColor, radius = maxRad * 0.9f, center = center, style = Stroke(width = 2f))
    }
}

@Composable
fun RadarRipples(maxRadius: Dp) {
    val infiniteTransition = rememberInfiniteTransition()
    
    // Create 3 expanding ripples with staggered start times
    val ripple1 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Restart)
    )
    val ripple2 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing, delayMillis = 1000), repeatMode = RepeatMode.Restart)
    )
    val ripple3 = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing, delayMillis = 2000), repeatMode = RepeatMode.Restart)
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = center
        val maxRad = maxRadius.toPx()

        listOf(ripple1.value, ripple2.value, ripple3.value).forEach { progress ->
            if (progress > 0f) {
                // Fade out as it expands
                val alpha = (1f - progress) * 0.5f 
                drawCircle(
                    color = primaryColor.copy(alpha = alpha),
                    radius = maxRad * progress,
                    center = center,
                    style = Stroke(width = 4f)
                )
            }
        }
    }
}
