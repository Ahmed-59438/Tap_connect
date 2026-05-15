package com.tapconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.ui.components.RadarView
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

@Composable
fun NetworkingScreen(
    onConnectionSuccess: (connectedName: String, icebreaker: String) -> Unit = { _, _ -> },
    viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isNetworkingOn by viewModel.isNetworkingModeEnabled.collectAsState()
    val nearbyUsers by viewModel.nearbyUsers.collectAsState()

    // Controls the Tap-to-Connect bottom sheet
    var showTapModal by remember { mutableStateOf(false) }
    var pendingConnectionRequest by remember { mutableStateOf<TapConnectionRequest?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TapConnect",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Networking Mode", style = MaterialTheme.typography.titleSmall)
            Switch(
                checked = isNetworkingOn,
                onCheckedChange = { viewModel.toggleNetworkingMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        RadarView(
            isScanning = isNetworkingOn,
            users = nearbyUsers,
            modifier = Modifier.weight(1f)
        )

        if (isNetworkingOn) {
            Text(
                "${nearbyUsers.size} people nearby",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // DEV TEST BUTTON: Simulate NFC tap (remove in production)
            OutlinedButton(
                onClick = {
                    pendingConnectionRequest = TapConnectionRequest(
                        userId = "test_user_1",
                        name = "Sarah Ali",
                        role = "Android Engineer",
                        organization = "Google"
                    )
                    showTapModal = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📲  Simulate NFC Tap (Dev)")
            }
        } else {
            Text(
                text = "Turn on Networking Mode to start connecting",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Show Tap-to-Connect modal when triggered
    if (showTapModal && pendingConnectionRequest != null) {
        TapConnectModal(
            connectionRequest = pendingConnectionRequest!!,
            onConnect = {
                showTapModal = false
                // Navigate to icebreaker with mock AI response
                onConnectionSuccess(
                    pendingConnectionRequest!!.name,
                    "You both build in Kotlin. Ask ${pendingConnectionRequest!!.name} about her experience with Jetpack Compose animations!"
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

