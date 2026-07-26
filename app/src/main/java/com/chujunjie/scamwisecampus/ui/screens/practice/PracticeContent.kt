package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

@Composable
internal fun PracticeContent(
    uiState: PracticeUiState,
    onCategorySelected: (ScamCategory?) -> Unit,
    onDifficultySelected: (Difficulty?) -> Unit,
    onClearFilters: () -> Unit,
    onScenarioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        PracticeHeader()

        Text(
            text = stringResource(
                R.string.practice_category_label
            ),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 20.dp)
        )

        CategoryFilterRow(
            selectedCategory =
                uiState.selectedCategory,
            onCategorySelected =
                onCategorySelected,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = stringResource(
                R.string.practice_difficulty_label
            ),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        DifficultyFilterRow(
            selectedDifficulty =
                uiState.selectedDifficulty,
            onDifficultySelected =
                onDifficultySelected,
            modifier = Modifier.padding(top = 8.dp)
        )

        ScenarioCountHeader(
            scenarioCount = uiState.scenarios.size,
            hasActiveFilters =
                uiState.selectedCategory != null ||
                    uiState.selectedDifficulty != null,
            onClearFilters = onClearFilters
        )

        if (uiState.scenarios.isEmpty()) {
            EmptyPracticeContent(
                onClearFilters = onClearFilters,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            ScenarioList(
                scenarios = uiState.scenarios,
                onScenarioClick = onScenarioClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PracticeHeader() {
    Text(
        text = stringResource(
            R.string.practice_title
        ),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.semantics {
            heading()
        }
    )

    Text(
        text = stringResource(
            R.string.practice_description
        ),
        style = MaterialTheme.typography.bodyMedium,
        color =
            MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun ScenarioCountHeader(
    scenarioCount: Int,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                bottom = 8.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = pluralStringResource(
                R.plurals.practice_scenario_count,
                scenarioCount,
                scenarioCount
            ),
            style = MaterialTheme.typography.titleMedium
        )

        if (hasActiveFilters) {
            TextButton(
                onClick = onClearFilters
            ) {
                Text(
                    text = stringResource(
                        R.string.practice_clear_filters
                    )
                )
            }
        }
    }
}