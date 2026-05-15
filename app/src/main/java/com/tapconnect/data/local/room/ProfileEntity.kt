package com.tapconnect.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val profileImage: String?,
    val bio: String,
    val role: String,
    val organization: String,
    val interests: String, // Stored as comma-separated or JSON string for simplicity in MVP
    val socialLinks: String, // JSON string
    val sharingPreferences: String // JSON string
)
