package com.luisfagundes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "user_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_NAME
)

class UserPreferences @Inject constructor(
    private val context: Context
) {
    fun shouldShowLocationRationale(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SHOULD_SHOW_LOCATION_RATIONALE] != false
        }

    suspend fun setShouldShowLocationRationale(shouldShow: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOULD_SHOW_LOCATION_RATIONALE] = shouldShow
        }
    }

    companion object {
        private val SHOULD_SHOW_LOCATION_RATIONALE = booleanPreferencesKey(
            "SHOULD_SHOW_LOCATION_RATIONALE"
        )
    }
}
