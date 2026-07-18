package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario

@Composable
fun PracticeScreen(
    uiState: PracticeUiState,
    onCategorySelected: (ScamCategory?) -> Unit,
    onDifficultySelected: (Difficulty?) -> Unit,
    onClearFilters: () -> Unit,
    onScenarioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        LoadingContent(
            modifier = modifier
        )

        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Practice",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "Build safer digital judgement through realistic scenarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "Category",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 20.dp)
        )

        CategoryFilterRow(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Difficulty",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        DifficultyFilterRow(
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultySelected = onDifficultySelected,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${uiState.scenarios.size} scenarios",
                style = MaterialTheme.typography.titleMedium
            )

            if (
                uiState.selectedCategory != null ||
                uiState.selectedDifficulty != null
            ) {
                TextButton(
                    onClick = onClearFilters
                ) {
                    Text(text = "Clear Filters")
                }
            }
        }

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
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = "Loading practice scenarios...",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: ScamCategory?,
    onCategorySelected: (ScamCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = {
                    onCategorySelected(null)
                },
                label = {
                    Text(text = "All")
                }
            )
        }

        items(
            items = ScamCategory.entries,
            key = { category -> category.name }
        ) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    onCategorySelected(category)
                },
                label = {
                    Text(text = category.displayName())
                }
            )
        }
    }
}

@Composable
private fun DifficultyFilterRow(
    selectedDifficulty: Difficulty?,
    onDifficultySelected: (Difficulty?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedDifficulty == null,
                onClick = {
                    onDifficultySelected(null)
                },
                label = {
                    Text(text = "All")
                }
            )
        }

        items(
            items = Difficulty.entries,
            key = { difficulty -> difficulty.name }
        ) { difficulty ->
            FilterChip(
                selected = selectedDifficulty == difficulty,
                onClick = {
                    onDifficultySelected(difficulty)
                },
                label = {
                    Text(text = difficulty.displayName())
                }
            )
        }
    }
}

@Composable
private fun ScenarioList(
    scenarios: List<Scenario>,
    onScenarioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = scenarios,
            key = { scenario -> scenario.id }
        ) { scenario ->
            ScenarioCard(
                scenario = scenario,
                onClick = {
                    onScenarioClick(scenario.id)
                }
            )
        }
    }
}

@Composable
private fun ScenarioCard(
    scenario: Scenario,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${scenario.category.displayName()} • ${scenario.difficulty.displayName()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "From: ${scenario.sender}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = scenario.messageBody,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun EmptyPracticeContent(
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No scenarios match the selected filters.",
            style = MaterialTheme.typography.bodyLarge
        )

        TextButton(
            onClick = onClearFilters,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Clear Filters")
        }
    }
}

private fun ScamCategory.displayName(): String {
    return when (this) {
        ScamCategory.JOB -> "Job"
        ScamCategory.BANKING -> "Banking"
        ScamCategory.PARCEL -> "Parcel"
        ScamCategory.MARKETPLACE -> "Marketplace"
        ScamCategory.IMPERSONATION -> "Impersonation"
        ScamCategory.PHISHING -> "Phishing"
    }
}

private fun Difficulty.displayName(): String {
    return when (this) {
        Difficulty.EASY -> "Easy"
        Difficulty.MEDIUM -> "Medium"
        Difficulty.HARD -> "Hard"
    }
}