package com.tapconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TapConnectionRequest(val userId: String, val name: String, val role: String, val organization: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TapConnectModal(
    connectionRequest: TapConnectionRequest,
    onConnect: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardBg,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            Text("Connect with ${connectionRequest.name}?",
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextH)
            Spacer(Modifier.height(4.dp))
            Text("${connectionRequest.role} at ${connectionRequest.organization}",
                fontSize = 14.sp, color = TextSub)
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = onConnect,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)
            ) {
                Text("Connect", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
            }
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Dismiss", color = TextSub, fontSize = 15.sp)
            }
        }
    }
}
