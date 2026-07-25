package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
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
            LoadingContent(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.practice_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                Text(
                    text = stringResource(R.string.practice_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = stringResource(R.string.practice_category_label),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 20.dp)
                )

                CategoryFilterRow(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelected,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = stringResource(R.string.practice_difficulty_label),
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
                        text = pluralStringResource(
                            R.plurals.practice_scenario_count,
                            uiState.scenarios.size,
                            uiState.scenarios.size
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (
                        uiState.selectedCategory != null ||
                        uiState.selectedDifficulty != null
                    ) {
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
            text = stringResource(R.string.practice_loading_scenarios),
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
                    Text(text = stringResource(R.string.practice_all))
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
                    Text(text = stringResource(category.displayNameRes()))
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
                    Text(text = stringResource(R.string.practice_all))
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
                    Text(text = stringResource(difficulty.displayNameRes()))
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
                text = stringResource(
                    R.string.scenario_category_difficulty_format,
                    stringResource(scenario.category.displayNameRes()),
                    stringResource(scenario.difficulty.displayNameRes())
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(
                    R.string.practice_sender_format,
                    scenario.sender
                ),
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
            text = stringResource(R.string.practice_no_matching_scenarios),
            style = MaterialTheme.typography.bodyLarge
        )

        TextButton(
            onClick = onClearFilters,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = stringResource(R.string.practice_clear_filters))
        }
    }
}

@StringRes
private fun ScamCategory.displayNameRes(): Int {
    return when (this) {
        ScamCategory.JOB -> R.string.category_job
        ScamCategory.BANKING -> R.string.category_banking
        ScamCategory.PARCEL -> R.string.category_parcel
        ScamCategory.MARKETPLACE -> R.string.category_marketplace
        ScamCategory.IMPERSONATION -> R.string.category_impersonation
        ScamCategory.PHISHING -> R.string.category_phishing
    }
}

@StringRes
private fun Difficulty.displayNameRes(): Int {
    return when (this) {
        Difficulty.EASY -> R.string.difficulty_easy
        Difficulty.MEDIUM -> R.string.difficulty_medium
        Difficulty.HARD -> R.string.difficulty_hard
    }
}