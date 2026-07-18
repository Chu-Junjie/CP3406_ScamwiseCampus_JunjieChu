package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun PracticeRoute(
    viewModel: PracticeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PracticeScreen(
        uiState = uiState,
        onCategorySelected = viewModel::selectCategory,
        onDifficultySelected = viewModel::selectDifficulty,
        onClearFilters = viewModel::clearFilters
    )
}