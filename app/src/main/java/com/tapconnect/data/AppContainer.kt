package com.tapconnect.data

import android.content.Context
import androidx.room.Room
import com.tapconnect.data.local.datastore.SettingsDataStore
import com.tapconnect.data.local.room.AppDatabase
import com.tapconnect.data.repository.ProfileRepository

/**
 * Manual Dependency Injection container for the application.
 * In a larger production app, this would be replaced by Dagger/Hilt or Koin.
 */
class AppContainer(private val context: Context) {

    // 1. Initialize the Room Database
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tapconnect_database"
        ).build()
    }

    // 2. Initialize the DataStore for settings
    val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStore(context)
    }

    // 3. Provide the Repositories
    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(database.profileDao())
    }
}
