package com.chujunjie.scamwisecampus.domain.repository

import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun observeThemeMode(): Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)
}
