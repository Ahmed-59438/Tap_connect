package com.tapconnect.domain.model

data class UserProfile(
    val userId: String,
    val name: String,
    val profileImage: String? = null,
    val bio: String,
    val role: String,
    val organization: String,
    val interests: List<String>,
    val socialLinks: Map<String, String>,
    val sharingPreferences: Map<String, Boolean>
)
