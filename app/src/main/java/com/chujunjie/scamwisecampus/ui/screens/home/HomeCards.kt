package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.Scenario

@Composable
internal fun HomeProgressCard(
    uiState: HomeUiState
) {
    HomeDashboardCard(
        title = stringResource(
            R.string.home_learning_progress_title
        )
    ) {
        HomeSummaryRow(
            label = stringResource(
                R.string.home_completed_attempts
            ),
            value = uiState.totalAttempts.toString()
        )

        HomeSummaryRow(
            label = stringResource(
                R.string.home_unique_scenarios
            ),
            value = stringResource(
                R.string.fraction_format,
                uiState.completedScenarioCount,
                uiState.totalScenarioCount
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        HomeSummaryRow(
            label = stringResource(
                R.string.home_average_score
            ),
            value = if (uiState.totalAttempts == 0) {
                stringResource(
                    R.string.common_not_available
                )
            } else {
                stringResource(
                    R.string.score_out_of_100,
                    uiState.averageScore
                )
            },
            modifier = Modifier.padding(top = 10.dp)
        )

        LinearProgressIndicator(
            progress = {
                uiState.completionPercent / 100f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Text(
            text = stringResource(
                R.string.home_completion_percent,
                uiState.completionPercent
            ),
            style =
                MaterialTheme.typography.labelLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
internal fun HomeRecommendationCard(
    scenario: Scenario,
    hasAttemptHistory: Boolean,
    onStartClick: () -> Unit
) {
    HomeDashboardCard(
        title = stringResource(
            if (hasAttemptHistory) {
                R.string.home_adaptive_recommendation
            } else {
                R.string.home_start_first_practice
            }
        )
    ) {
        Text(
            text = scenario.title,
            style =
                MaterialTheme.typography.headlineSmall
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
                MaterialTheme.typography.labelLarge,
            color =
                MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 6.dp)
        )

        Text(
            text = stringResource(
                if (hasAttemptHistory) {
                    R.string
                        .home_recommendation_existing
                } else {
                    R.string.home_recommendation_new
                }
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 10.dp)
        )

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string
                        .home_start_recommended_practice
                )
            )
        }
    }
}

@Composable
internal fun HomeLatestResultCard(
    attempt: AttemptRecord
) {
    HomeDashboardCard(
        title = stringResource(
            R.string.home_latest_result_title
        )
    ) {
        HomeSummaryRow(
            label = stringResource(
                attempt.category.displayNameRes()
            ),
            value = stringResource(
                R.string.score_out_of_100,
                attempt.totalScore
            )
        )

        Text(
            text = stringResource(
                attempt.confidenceCalibration
                    .displayNameRes()
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
internal fun HomeQuickActionsCard(
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLinkVerificationClick: () -> Unit
) {
    HomeDashboardCard(
        title = stringResource(
            R.string.home_quick_actions_title
        )
    ) {
        Button(
            onClick = onPracticeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    R.string.home_browse_all_scenarios
                )
            )
        }

        OutlinedButton(
            onClick = onStatisticsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.home_view_statistics
                )
            )
        }

        OutlinedButton(
            onClick = onLinkVerificationClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = stringResource(
                    R.string
                        .home_open_link_verification
                )
            )
        }
    }
}

@Composable
internal fun HomePrivacyCard() {
    HomeDashboardCard(
        title = stringResource(
            R.string.home_privacy_title
        )
    ) {
        Text(
            text = stringResource(
                R.string.home_privacy_description
            ),
            style =
                MaterialTheme.typography.bodyMedium
        )
    }
}