package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R

private val WideStatisticsBreakpoint = 600.dp

@Composable
internal fun StatisticsDashboard(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val useTwoColumns =
            maxWidth >= WideStatisticsBreakpoint

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                StatisticsHeader()
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
                AccuracyAndCalibrationSection(
                    uiState = uiState,
                    useTwoColumns = useTwoColumns
                )
            }

            item {
                SectionHeading(
                    text = stringResource(
                        R.string
                            .statistics_performance_by_category
                    )
                )
            }

            if (useTwoColumns) {
                items(
                    items =
                        uiState.categoryPerformance.chunked(2),
                    key = { row ->
                        row.joinToString(separator = "-") {
                            performance ->
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
                                modifier =
                                    Modifier.weight(1f)
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
                        row.joinToString(separator = "-") {
                            attempt ->
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
                                modifier =
                                    Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.recentAttempts,
                    key = { attempt ->
                        attempt.attemptId
                    }
                ) { attempt ->
                    RecentAttemptCard(
                        attempt = attempt
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsHeader() {
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

@Composable
private fun AccuracyAndCalibrationSection(
    uiState: StatisticsUiState,
    useTwoColumns: Boolean
) {
    if (useTwoColumns) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            AccuracyCard(
                riskAccuracyPercent =
                    uiState.riskAccuracyPercent,
                safeActionAccuracyPercent =
                    uiState.safeActionAccuracyPercent,
                modifier = Modifier.weight(1f)
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

            CalibrationCard(
                uiState = uiState
            )
        }
    }
}