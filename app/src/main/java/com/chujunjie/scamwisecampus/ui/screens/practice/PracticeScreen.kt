package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun PracticeScreen(
    uiState: PracticeUiState,
    onCategorySelected: (ScamCategory?) -> Unit,
    onDifficultySelected: (Difficulty?) -> Unit,
    onClearFilters: () -> Unit,
    onScenarioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        if (uiState.isLoading) {
            LoadingPracticeContent(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PracticeContent(
                uiState = uiState,
                onCategorySelected = onCategorySelected,
                onDifficultySelected =
                    onDifficultySelected,
                onClearFilters = onClearFilters,
                onScenarioClick = onScenarioClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}