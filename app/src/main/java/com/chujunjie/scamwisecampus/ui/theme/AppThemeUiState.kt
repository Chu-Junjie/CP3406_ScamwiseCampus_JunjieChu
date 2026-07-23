package com.chujunjie.scamwisecampus.ui.theme

import com.chujunjie.scamwisecampus.domain.model.ThemeMode

data class AppThemeUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLoading: Boolean = true
)
