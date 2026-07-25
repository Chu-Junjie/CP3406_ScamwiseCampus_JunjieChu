package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

private val WideStatisticsBreakpoint = 600.dp
private val StatisticsColumnSpacing = 16.dp

@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> {
                LoadingStatisticsContent(
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.errorMessage != null -> {
                StatisticsMessageContent(
                    message = uiState.errorMessage,
                    modifier = Modifier.fillMaxSize()
                )
            }

            !uiState.hasAttempts -> {
                EmptyStatisticsContent(
                    onPracticeClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                StatisticsDashboard(
                    uiState = uiState,
                    onPracticeClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun StatisticsDashboard(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val useTwoColumns = maxWidth >= WideStatisticsBreakpoint

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                    text = stringResource(
                        R.string.statistics_title
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                Text(
                    text = stringResource(
                        R.string.statistics_description
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                SummaryCard(
                    totalAttempts = uiState.totalAttempts,
                    averageScore = uiState.averageScore,
                    highestScore = uiState.highestScore
                )
            }

            uiState.recommendedCategory?.let { category ->
                item {
                    RecommendationCard(
                        category = category,
                        onPracticeClick = onPracticeClick
                    )
                }
            }

            item {
                if (useTwoColumns) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AccuracyCard(
                            riskAccuracyPercent =
                                uiState.riskAccuracyPercent,
                            safeActionAccuracyPercent =
                                uiState.safeActionAccuracyPercent,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(
                            modifier = Modifier.width(
                                StatisticsColumnSpacing
                            )
                        )

                        CalibrationCard(
                            uiState = uiState,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(16.dp)
                    ) {
                        AccuracyCard(
                            riskAccuracyPercent =
                                uiState.riskAccuracyPercent,
                            safeActionAccuracyPercent =
                                uiState.safeActionAccuracyPercent
                        )

                        CalibrationCard(uiState = uiState)
                    }
                }
            }

            item {
                SectionHeading(
                    text = stringResource(
                        R.string.statistics_performance_by_category
                    )
                )
            }

            if (useTwoColumns) {
                items(
                    items = uiState.categoryPerformance.chunked(2),
                    key = { row ->
                        row.joinToString(separator = "-") { performance ->
                            performance.category.name
                        }
                    }
                ) { row ->
                    TwoColumnRow(
                        itemCount = row.size
                    ) {
                        row.forEach { performance ->
                            CategoryPerformanceCard(
                                performance = performance,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.categoryPerformance,
                    key = { performance ->
                        performance.category.name
                    }
                ) { performance ->
                    CategoryPerformanceCard(
                        performance = performance
                    )
                }
            }

            item {
                SectionHeading(
                    text = stringResource(
                        R.string.statistics_recent_attempts
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (useTwoColumns) {
                items(
                    items = uiState.recentAttempts.chunked(2),
                    key = { row ->
                        row.joinToString(separator = "-") { attempt ->
                            attempt.attemptId.toString()
                        }
                    }
                ) { row ->
                    TwoColumnRow(
                        itemCount = row.size
                    ) {
                        row.forEach { attempt ->
                            RecentAttemptCard(
                                attempt = attempt,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.recentAttempts,
                    key = { attempt -> attempt.attemptId }
                ) { attempt ->
                    RecentAttemptCard(attempt = attempt)
                }
            }
        }
    }
}

@Composable
private fun TwoColumnRow(
    itemCount: Int,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            StatisticsColumnSpacing
        )
    ) {
        content()

        if (itemCount == 1) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SectionHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.semantics {
            heading()
        }
    )
}

@Composable
private fun SummaryCard(
    totalAttempts: Int,
    averageScore: Int,
    highestScore: Int
) {
    StatisticsCard(
        title = stringResource(
            R.string.statistics_overall_progress
        )
    ) {
        SummaryValueRow(
            label = stringResource(
                R.string.statistics_completed_attempts
            ),
            value = totalAttempts.toString()
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SummaryValueRow(
            label = stringResource(
                R.string.statistics_average_score
            ),
            value = stringResource(
                R.string.score_out_of_100,
                averageScore
            )
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SummaryValueRow(
            label = stringResource(
                R.string.statistics_highest_score
            ),
            value = stringResource(
                R.string.score_out_of_100,
                highestScore
            )
        )
    }
}

@Composable
private fun RecommendationCard(
    category: ScamCategory,
    onPracticeClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.statistics_recommended_practice
                ),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    category.displayNameRes()
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = stringResource(
                    R.string
                        .statistics_recommendation_description
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = onPracticeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.statistics_practice_now
                    )
                )
            }
        }
    }
}

@Composable
private fun AccuracyCard(
    riskAccuracyPercent: Int,
    safeActionAccuracyPercent: Int,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = stringResource(
            R.string.statistics_decision_accuracy
        ),
        modifier = modifier
    ) {
        ProgressMetric(
            label = stringResource(
                R.string.statistics_risk_assessment_accuracy
            ),
            percent = riskAccuracyPercent
        )

        ProgressMetric(
            label = stringResource(
                R.string.statistics_safe_action_accuracy
            ),
            percent = safeActionAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun CalibrationCard(
    uiState: StatisticsUiState,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = stringResource(
            R.string.statistics_confidence_calibration
        ),
        modifier = modifier
    ) {
        CalibrationRow(
            label = stringResource(
                ConfidenceCalibration.WELL_CALIBRATED
                    .displayNameRes()
            ),
            count = uiState.wellCalibratedCount
        )

        CalibrationRow(
            label = stringResource(
                ConfidenceCalibration.UNDERCONFIDENT
                    .displayNameRes()
            ),
            count = uiState.underconfidentCount
        )

        CalibrationRow(
            label = stringResource(
                ConfidenceCalibration.OVERCONFIDENT
                    .displayNameRes()
            ),
            count = uiState.overconfidentCount
        )

        CalibrationRow(
            label = stringResource(
                ConfidenceCalibration.NEEDS_REVIEW
                    .displayNameRes()
            ),
            count = uiState.needsReviewCount
        )

        CalibrationRow(
            label = stringResource(
                ConfidenceCalibration
                    .CAUTIOUS_BUT_INCORRECT
                    .displayNameRes()
            ),
            count = uiState.cautiousButIncorrectCount
        )
    }
}

@Composable
private fun CategoryPerformanceCard(
    performance: CategoryPerformance,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = stringResource(
            performance.category.displayNameRes()
        ),
        modifier = modifier
    ) {
        SummaryValueRow(
            label = stringResource(
                R.string.statistics_attempts
            ),
            value = performance.attemptCount.toString()
        )

        SummaryValueRow(
            label = stringResource(
                R.string.statistics_average_score
            ),
            value = stringResource(
                R.string.score_out_of_100,
                performance.averageScore
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        SummaryValueRow(
            label = stringResource(
                R.string.statistics_highest_score
            ),
            value = stringResource(
                R.string.score_out_of_100,
                performance.highestScore
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        ProgressMetric(
            label = stringResource(
                R.string.statistics_risk_accuracy
            ),
            percent = performance.riskAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )

        ProgressMetric(
            label = stringResource(
                R.string.statistics_safe_action_accuracy_short
            ),
            percent =
                performance.safeActionAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun RecentAttemptCard(
    attempt: AttemptRecord,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        attempt.category.displayNameRes()
                    ),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.score_out_of_100,
                        attempt.totalScore
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(
                    attempt.difficulty.displayNameRes()
                ),
                style = MaterialTheme.typography.labelLarge,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(
                    attempt.confidenceCalibration.displayNameRes()
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
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
private fun SummaryValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Read the metric label and value together.
            },
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ProgressMetric(
    label: String,
    percent: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Read the metric label and percentage together.
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(
                    R.string.percentage_format,
                    percent
                ),
                style = MaterialTheme.typography.labelLarge
            )
        }

        LinearProgressIndicator(
            progress = {
                percent.coerceIn(
                    minimumValue = 0,
                    maximumValue = 100
                ) / 100f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clearAndSetSemantics {
                    // The visible percentage already describes progress.
                }
        )
    }
}

@Composable
private fun CalibrationRow(
    label: String,
    count: Int
) {
    SummaryValueRow(
        label = label,
        value = count.toString(),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun LoadingStatisticsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = stringResource(
                R.string.statistics_loading
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun EmptyStatisticsContent(
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.statistics_no_history_title
            ),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = stringResource(
                R.string.statistics_no_history_description
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = onPracticeClick,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.statistics_start_practice
                )
            )
        }
    }
}

@Composable
private fun StatisticsMessageContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Assertive
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.statistics_unavailable
            ),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@StringRes
private fun ScamCategory.displayNameRes(): Int {
    return when (this) {
        ScamCategory.JOB -> R.string.category_job
        ScamCategory.BANKING -> R.string.category_banking
        ScamCategory.PARCEL -> R.string.category_parcel
        ScamCategory.MARKETPLACE ->
            R.string.category_marketplace

        ScamCategory.IMPERSONATION ->
            R.string.category_impersonation

        ScamCategory.PHISHING ->
            R.string.category_phishing
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