package com.tapconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapconnect.data.local.datastore.SettingsDataStore
import com.tapconnect.ui.components.RadarUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val isNetworkingModeEnabled: StateFlow<Boolean> = settingsDataStore.isNetworkingModeEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _nearbyUsers = MutableStateFlow<List<RadarUser>>(emptyList())
    val nearbyUsers: StateFlow<List<RadarUser>> = _nearbyUsers.asStateFlow()

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
        // Mocking users appearing at different locations on the radar
        _nearbyUsers.value = listOf(
            RadarUser("1", "Alex", 0.4f, 45f), // Top right, close
            RadarUser("2", "Sarah", 0.8f, 135f), // Bottom right, far
            RadarUser("3", "John", 0.6f, 250f) // Left, medium distance
        )
    }

    private fun stopScanning() {
        _nearbyUsers.value = emptyList()
    }
}
