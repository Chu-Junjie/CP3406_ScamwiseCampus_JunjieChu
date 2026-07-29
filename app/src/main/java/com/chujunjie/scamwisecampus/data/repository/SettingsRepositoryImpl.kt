package com.chujunjie.scamwisecampus.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override fun observeThemeMode(): Flow<ThemeMode> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[THEME_MODE_KEY]
                    ?.toThemeMode()
                    ?: ThemeMode.SYSTEM
            }
    }

    override suspend fun setThemeMode(
        themeMode: ThemeMode
    ) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] =
                themeMode.name
        }
    }

    private fun String.toThemeMode(): ThemeMode {
        return runCatching {
            ThemeMode.valueOf(this)
        }.getOrDefault(
            ThemeMode.SYSTEM
        )
    }

    private companion object {
        val THEME_MODE_KEY =
            stringPreferencesKey("theme_mode")
    }
}
