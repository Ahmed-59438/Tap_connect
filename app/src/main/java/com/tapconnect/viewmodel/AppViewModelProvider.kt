package com.tapconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.tapconnect.TapConnectApplication
import com.tapconnect.data.repository.ProfileRepository

/**
 * Factory for all ViewModels, handling dependency injection manually.
 */
object AppViewModelProvider {
    val Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            // Get the application object
            val application = checkNotNull(extras[APPLICATION_KEY]) as TapConnectApplication
            val container = application.container

            return when {
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(container.profileRepository) as T
                }
                modelClass.isAssignableFrom(DiscoveryViewModel::class.java) -> {
                    DiscoveryViewModel(container.settingsDataStore) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
