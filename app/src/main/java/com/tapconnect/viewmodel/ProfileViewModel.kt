package com.tapconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapconnect.data.repository.ProfileRepository
import com.tapconnect.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // In MVP, we just collect from the local DB flow
            profileRepository.getProfile(userId).collect { profile ->
                _userProfile.value = profile
                _isLoading.value = false
            }
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            profileRepository.saveProfile(profile)
            // Once saved, it will automatically update _userProfile via the collect block above 
            // if we are observing that specific user ID.
        }
    }
}
