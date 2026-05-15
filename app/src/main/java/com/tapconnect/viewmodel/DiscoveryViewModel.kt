package com.tapconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapconnect.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // Read the toggle state from DataStore, defaulting to false
    val isNetworkingModeEnabled: StateFlow<Boolean> = settingsDataStore.isNetworkingModeEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _nearbyUsers = MutableStateFlow<List<String>>(emptyList())
    val nearbyUsers: StateFlow<List<String>> = _nearbyUsers.asStateFlow()

    fun toggleNetworkingMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setNetworkingMode(enabled)
            
            if (enabled) {
                startScanning()
            } else {
                stopScanning()
            }
        }
    }

    private fun startScanning() {
        // Placeholder for BLE/NFC scanning logic
        _nearbyUsers.value = listOf("Alex (Designer)", "Sarah (Founder)", "John (Android Dev)")
    }

    private fun stopScanning() {
        // Placeholder for stopping BLE/NFC
        _nearbyUsers.value = emptyList()
    }
}
