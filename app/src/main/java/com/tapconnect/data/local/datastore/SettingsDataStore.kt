package com.tapconnect.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {
    private val dataStore = context.dataStore

    val isNetworkingModeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NETWORKING_MODE_KEY] ?: false
    }

    suspend fun setNetworkingMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NETWORKING_MODE_KEY] = enabled
        }
    }

    companion object {
        private val NETWORKING_MODE_KEY = booleanPreferencesKey("networking_mode")
    }
}
