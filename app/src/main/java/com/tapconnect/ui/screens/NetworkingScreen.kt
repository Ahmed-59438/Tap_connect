package com.tapconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.ui.components.RadarView
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.DiscoveryViewModel

@Composable
fun NetworkingScreen(
    viewModel: DiscoveryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isNetworkingOn by viewModel.isNetworkingModeEnabled.collectAsState()
    val nearbyUsers by viewModel.nearbyUsers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TapConnect",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
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

        Spacer(modifier = Modifier.height(32.dp))

        // Radar UI replacing the old list
        RadarView(
            isScanning = isNetworkingOn,
            users = nearbyUsers,
            modifier = Modifier.weight(1f)
        )

        if (isNetworkingOn) {
            Text(
                "Scanning for nearby connections...", 
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            Text(
                text = "Turn on Networking Mode to start connecting", 
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
