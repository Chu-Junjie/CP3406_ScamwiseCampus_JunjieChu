package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLinkVerificationClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onScenarioClick = onScenarioClick,
        onPracticeClick = onPracticeClick,
        onStatisticsClick = onStatisticsClick,
        onLinkVerificationClick =
            onLinkVerificationClick
    )
}