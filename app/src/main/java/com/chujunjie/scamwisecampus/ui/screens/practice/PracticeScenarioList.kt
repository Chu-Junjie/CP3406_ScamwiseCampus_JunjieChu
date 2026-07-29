package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.ui.components.ScenarioSourceLabel

@Composable
internal fun ScenarioList(
    scenarios: List<Scenario>,
    onScenarioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding =
            PaddingValues(bottom = 24.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = scenarios,
            key = { scenario ->
                scenario.id
            }
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
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    R.string
                        .scenario_category_difficulty_format,
                    stringResource(
                        scenario.category.displayNameRes()
                    ),
                    stringResource(
                        scenario.difficulty.displayNameRes()
                    )
                ),
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            ScenarioSourceLabel(
                sender = scenario.sender,
                category = scenario.category,
                showFromPrefix = true,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = scenario.messageBody,
                style =
                    MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}