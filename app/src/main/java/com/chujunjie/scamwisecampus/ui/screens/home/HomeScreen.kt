package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLinkVerificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> {
                HomeMessageContent(
                    message = stringResource(R.string.home_loading_dashboard),
                    showProgress = true,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.errorMessage != null -> {
                HomeMessageContent(
                    message = uiState.errorMessage,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                HomeDashboard(
                    uiState = uiState,
                    onScenarioClick = onScenarioClick,
                    onPracticeClick = onPracticeClick,
                    onStatisticsClick = onStatisticsClick,
                    onLinkVerificationClick = onLinkVerificationClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun HomeDashboard(
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = stringResource(R.string.home_tagline),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(R.string.home_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            ProgressCard(uiState = uiState)
        }

        uiState.recommendedScenario?.let { scenario ->
            item {
                RecommendationCard(
                    scenario = scenario,
                    hasAttemptHistory = uiState.totalAttempts > 0,
                    onStartClick = {
                        onScenarioClick(scenario.id)
                    }
                )
            }
        }

        uiState.latestAttempt?.let { attempt ->
            item {
                DashboardCard(
                    title = stringResource(R.string.home_latest_result_title)
                ) {
                    SummaryRow(
                        label = stringResource(attempt.category.displayNameRes()),
                        value = stringResource(
                            R.string.score_out_of_100,
                            attempt.totalScore
                        )
                    )

                    Text(
                        text = stringResource(
                            attempt.confidenceCalibration.displayNameRes()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        item {
            DashboardCard(
                title = stringResource(R.string.home_quick_actions_title)
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
                            R.string.home_open_link_verification
                        )
                    )
                }
            }
        }

        item {
            DashboardCard(
                title = stringResource(R.string.home_privacy_title)
            ) {
                Text(
                    text = stringResource(R.string.home_privacy_description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ProgressCard(
    uiState: HomeUiState
) {
    DashboardCard(
        title = stringResource(R.string.home_learning_progress_title)
    ) {
        SummaryRow(
            label = stringResource(R.string.home_completed_attempts),
            value = uiState.totalAttempts.toString()
        )

        SummaryRow(
            label = stringResource(R.string.home_unique_scenarios),
            value = stringResource(
                R.string.fraction_format,
                uiState.completedScenarioCount,
                uiState.totalScenarioCount
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        SummaryRow(
            label = stringResource(R.string.home_average_score),
            value = if (uiState.totalAttempts == 0) {
                stringResource(R.string.common_not_available)
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
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun RecommendationCard(
    scenario: Scenario,
    hasAttemptHistory: Boolean,
    onStartClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = if (hasAttemptHistory) {
                    stringResource(R.string.home_adaptive_recommendation)
                } else {
                    stringResource(R.string.home_start_first_practice)
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = stringResource(
                    R.string.scenario_category_difficulty_format,
                    stringResource(scenario.category.displayNameRes()),
                    stringResource(scenario.difficulty.displayNameRes())
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = if (hasAttemptHistory) {
                    stringResource(R.string.home_recommendation_existing)
                } else {
                    stringResource(R.string.home_recommendation_new)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        R.string.home_start_recommended_practice
                    )
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun HomeMessageContent(
    message: String,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showProgress) {
            CircularProgressIndicator()
        }

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            modifier = if (showProgress) {
                Modifier.padding(top = 16.dp)
            } else {
                Modifier
            }
        )
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

@StringRes
private fun ConfidenceCalibration.displayNameRes(): Int {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            R.string.calibration_well_calibrated

        ConfidenceCalibration.UNDERCONFIDENT ->
            R.string.calibration_underconfident

        ConfidenceCalibration.OVERCONFIDENT ->
            R.string.calibration_overconfident

        ConfidenceCalibration.NEEDS_REVIEW ->
            R.string.calibration_needs_review

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            R.string.calibration_cautious_but_incorrect
    }
}