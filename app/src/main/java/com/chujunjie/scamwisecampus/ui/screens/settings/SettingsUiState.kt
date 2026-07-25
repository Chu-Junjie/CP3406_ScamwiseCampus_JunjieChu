package com.chujunjie.scamwisecampus.ui.screens.settings

import com.chujunjie.scamwisecampus.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLoading: Boolean = true,
    val isClearingHistory: Boolean = false,
    val feedback: SettingsFeedback? = null
)

enum class SettingsFeedback {
    THEME_SAVE_FAILED,
    HISTORY_CLEARED,
    HISTORY_CLEAR_FAILED
}

