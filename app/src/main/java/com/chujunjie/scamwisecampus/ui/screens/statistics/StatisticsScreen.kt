package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion

@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            LoadingStatisticsContent(modifier = modifier)
        }

        uiState.errorMessage != null -> {
            StatisticsMessageContent(
                message = uiState.errorMessage,
                modifier = modifier
            )
        }

        !uiState.hasAttempts -> {
            EmptyStatisticsContent(
                onPracticeClick = onPracticeClick,
                modifier = modifier
            )
        }

        else -> {
            StatisticsDashboard(
                uiState = uiState,
                onPracticeClick = onPracticeClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun StatisticsDashboard(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
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
                text = "Statistics",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = "Review your practice history and identify where to improve.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            AccuracyCard(
                riskAccuracyPercent =
                    uiState.riskAccuracyPercent,
                safeActionAccuracyPercent =
                    uiState.safeActionAccuracyPercent
            )
        }

        item {
            CalibrationCard(uiState = uiState)
        }

        item {
            Text(
                text = "Performance by Category",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics {
                    heading()
                }
            )
        }

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

        item {
            Text(
                text = "Recent Attempts",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .semantics {
                        heading()
                    }
            )
        }

        items(
            items = uiState.recentAttempts,
            key = { attempt -> attempt.attemptId }
        ) { attempt ->
            RecentAttemptCard(attempt = attempt)
        }
    }
}

@Composable
private fun SummaryCard(
    totalAttempts: Int,
    averageScore: Int,
    highestScore: Int
) {
    StatisticsCard(title = "Overall Progress") {
        SummaryValueRow(
            label = "Completed attempts",
            value = totalAttempts.toString()
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SummaryValueRow(
            label = "Average score",
            value = "$averageScore / 100"
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SummaryValueRow(
            label = "Highest score",
            value = "$highestScore / 100"
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
                text = "Recommended Practice",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = category.displayName(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "This is currently your lowest-scoring practised category.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = onPracticeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Practice Now")
            }
        }
    }
}

@Composable
private fun AccuracyCard(
    riskAccuracyPercent: Int,
    safeActionAccuracyPercent: Int
) {
    StatisticsCard(title = "Decision Accuracy") {
        ProgressMetric(
            label = "Risk assessment accuracy",
            percent = riskAccuracyPercent
        )

        ProgressMetric(
            label = "Safe action accuracy",
            percent = safeActionAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun CalibrationCard(
    uiState: StatisticsUiState
) {
    StatisticsCard(title = "Confidence Calibration") {
        CalibrationRow(
            label = ConfidenceCalibration.WELL_CALIBRATED
                .displayName(),
            count = uiState.wellCalibratedCount
        )

        CalibrationRow(
            label = ConfidenceCalibration.UNDERCONFIDENT
                .displayName(),
            count = uiState.underconfidentCount
        )

        CalibrationRow(
            label = ConfidenceCalibration.OVERCONFIDENT
                .displayName(),
            count = uiState.overconfidentCount
        )

        CalibrationRow(
            label = ConfidenceCalibration.NEEDS_REVIEW
                .displayName(),
            count = uiState.needsReviewCount
        )

        CalibrationRow(
            label = ConfidenceCalibration
                .CAUTIOUS_BUT_INCORRECT
                .displayName(),
            count = uiState.cautiousButIncorrectCount
        )
    }
}

@Composable
private fun CategoryPerformanceCard(
    performance: CategoryPerformance
) {
    StatisticsCard(
        title = performance.category.displayName()
    ) {
        SummaryValueRow(
            label = "Attempts",
            value = performance.attemptCount.toString()
        )

        SummaryValueRow(
            label = "Average score",
            value = "${performance.averageScore} / 100",
            modifier = Modifier.padding(top = 10.dp)
        )

        SummaryValueRow(
            label = "Highest score",
            value = "${performance.highestScore} / 100",
            modifier = Modifier.padding(top = 10.dp)
        )

        ProgressMetric(
            label = "Risk accuracy",
            percent = performance.riskAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )

        ProgressMetric(
            label = "Safe action accuracy",
            percent =
                performance.safeActionAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun RecentAttemptCard(
    attempt: AttemptRecord
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
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
                    text = attempt.category.displayName(),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${attempt.totalScore} / 100",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = attempt.difficulty.displayName(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = attempt.confidenceCalibration.displayName(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun StatisticsCard(
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
            style =
                MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.titleMedium
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
                style =
                    MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "$percent%",
                style =
                    MaterialTheme.typography.labelLarge
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
                liveRegion =
                    LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = "Loading statistics...",
            modifier = Modifier.padding(
                top = 16.dp
            )
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
                liveRegion =
                    LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "No practice history yet",
            style =
                MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = "Complete a scenario to begin tracking your progress.",
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                top = 8.dp
            )
        )

        Button(
            onClick = onPracticeClick,
            modifier = Modifier.padding(
                top = 20.dp
            )
        ) {
            Text(text = "Start Practice")
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
                liveRegion =
                    LiveRegionMode.Assertive
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics unavailable",
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.error,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = message,
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(
                top = 8.dp
            )
        )
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

private fun ConfidenceCalibration.displayName(): String {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            "Well Calibrated"

        ConfidenceCalibration.UNDERCONFIDENT ->
            "Underconfident"

        ConfidenceCalibration.OVERCONFIDENT ->
            "Overconfident"

        ConfidenceCalibration.NEEDS_REVIEW ->
            "Needs Review"

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            "Cautious but Incorrect"
    }
}
