package com.santansarah.barcodescanner.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

data class AppPreferences(
    val hasAccount: Boolean = false,
    val hasVerifiedEmail: Boolean = false
)

class AppPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val HAS_ACCOUNT = booleanPreferencesKey("userId")
        val HAS_VERIFIED_EMAIL = booleanPreferencesKey("isSignedOut")
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    /**
     * Use this if you don't want to observe a flow.
     */
    suspend fun fetchInitialPreferences() =
        mapAppPreferences(dataStore.data.first().toPreferences())

    /**
     * Get the user preferences flow. When it's collected, keys are mapped to the
     * [UserPreferences] data class.
     */
    val appPreferencesFlow: Flow<AppPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.d("Error reading preferences: $exception")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapAppPreferences(preferences)
        }

    suspend fun setHasAccount(hasAccount: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_ACCOUNT] = hasAccount
        }
    }

    suspend fun setHasVerifiedEmail(hasVerifiedEmail: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_VERIFIED_EMAIL] = hasVerifiedEmail
        }
    }

    /**
     * Get the preferences key, then map it to the data class.
     */
    private fun mapAppPreferences(preferences: Preferences): AppPreferences {
        val hasAccount = preferences[PreferencesKeys.HAS_ACCOUNT] ?: false
        val hasVerifiedEmail = preferences[PreferencesKeys.HAS_VERIFIED_EMAIL] ?: false

        return AppPreferences(hasAccount = hasAccount, hasVerifiedEmail = hasVerifiedEmail)
    }
}