package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onThemeModeSelected =
            viewModel::selectThemeMode,
        onClearHistory =
            viewModel::clearPracticeHistory,
        onDismissFeedback =
            viewModel::dismissFeedback
    )
}
