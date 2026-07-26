package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R

@Composable
internal fun HomeDashboard(
    uiState: HomeUiState,
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLinkVerificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 20.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeHeader()
        }

        item {
            HomeProgressCard(
                uiState = uiState
            )
        }

        uiState.recommendedScenario?.let { scenario ->
            item {
                HomeRecommendationCard(
                    scenario = scenario,
                    hasAttemptHistory =
                        uiState.totalAttempts > 0,
                    onStartClick = {
                        onScenarioClick(scenario.id)
                    }
                )
            }
        }

        uiState.latestAttempt?.let { attempt ->
            item {
                HomeLatestResultCard(
                    attempt = attempt
                )
            }
        }

        item {
            HomeQuickActionsCard(
                onPracticeClick = onPracticeClick,
                onStatisticsClick =
                    onStatisticsClick,
                onLinkVerificationClick =
                    onLinkVerificationClick
            )
        }

        item {
            HomePrivacyCard()
        }
    }
}

@Composable
private fun HomeHeader() {
    Text(
        text = stringResource(
            R.string.app_name
        ),
        style =
            MaterialTheme.typography.headlineMedium,
        modifier = Modifier.semantics {
            heading()
        }
    )

    Text(
        text = stringResource(
            R.string.home_tagline
        ),
        style =
            MaterialTheme.typography.titleMedium,
        color =
            MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 4.dp)
    )

    Text(
        text = stringResource(
            R.string.home_description
        ),
        style =
            MaterialTheme.typography.bodyMedium,
        color =
            MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp)
    )
}