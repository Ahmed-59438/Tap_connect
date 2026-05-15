package com.tapconnect.data.repository

import com.tapconnect.data.local.room.ProfileDao
import com.tapconnect.data.local.room.ProfileEntity
import com.tapconnect.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(private val profileDao: ProfileDao) {

    // Get the profile and map it from the Database Entity to the Domain Model
    fun getProfile(userId: String): Flow<UserProfile?> {
        return profileDao.getProfile(userId).map { entity ->
            entity?.toDomainModel()
        }
    }

    // Save the profile: Map Domain Model to Database Entity
    suspend fun saveProfile(profile: UserProfile) {
        profileDao.insertProfile(profile.toEntity())
    }

    // Extension functions for mapping
    private fun ProfileEntity.toDomainModel(): UserProfile {
        return UserProfile(
            userId = this.userId,
            name = this.name,
            profileImage = this.profileImage,
            bio = this.bio,
            role = this.role,
            organization = this.organization,
            // In a real app, use Gson/Moshi to parse the JSON string back to lists/maps
            interests = this.interests.split(",").filter { it.isNotBlank() },
            socialLinks = emptyMap(), // Simplified for MVP
            sharingPreferences = emptyMap() // Simplified for MVP
        )
    }

    private fun UserProfile.toEntity(): ProfileEntity {
        return ProfileEntity(
            userId = this.userId,
            name = this.name,
            profileImage = this.profileImage,
            bio = this.bio,
            role = this.role,
            organization = this.organization,
            interests = this.interests.joinToString(","),
            socialLinks = "{}", // Simplified for MVP
            sharingPreferences = "{}" // Simplified for MVP
        )
    }
}
