package com.tapconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.ui.components.TapConnectCard
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

        Spacer(modifier = Modifier.height(32.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        if (isNetworkingOn) {
            Text("Scanning for nearby connections...", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Render discovered users
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(nearbyUsers) { user ->
                    TapConnectCard {
                        Text(user, style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Turn on Networking Mode to start connecting", 
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
