package com.chujunjie.scamwisecampus.ui.screens.home

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
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            HomeMessageContent(
                message = "Loading your learning dashboard...",
                showProgress = true,
                modifier = modifier
            )
        }

        uiState.errorMessage != null -> {
            HomeMessageContent(
                message = uiState.errorMessage,
                modifier = modifier
            )
        }

        else -> {
            HomeDashboard(
                uiState = uiState,
                onScenarioClick = onScenarioClick,
                onPracticeClick = onPracticeClick,
                onStatisticsClick = onStatisticsClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeDashboard(
    uiState: HomeUiState,
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
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
                text = "ScamWise Campus",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Pause. Check. Protect.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Practise realistic digital decisions and build safer judgement.",
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
                DashboardCard(
                    title = "Latest Result"
                ) {
                    SummaryRow(
                        label = attempt.category.displayName(),
                        value = "${attempt.totalScore} / 100"
                    )

                    Text(
                        text = attempt.confidenceCalibration
                            .displayName(),
                        style = MaterialTheme.typography.bodyMedium,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        item {
            DashboardCard(
                title = "Quick Actions"
            ) {
                Button(
                    onClick = onPracticeClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Browse All Scenarios")
                }

                OutlinedButton(
                    onClick = onStatisticsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(text = "View Statistics")
                }
            }
        }

        item {
            DashboardCard(
                title = "Privacy by Design"
            ) {
                Text(
                    text = "No account is required. Practice history stays in the app's private local storage and can be cleared from Settings.",
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
        title = "Learning Progress"
    ) {
        SummaryRow(
            label = "Completed attempts",
            value = uiState.totalAttempts.toString()
        )

        SummaryRow(
            label = "Unique scenarios",
            value = "${uiState.completedScenarioCount} / ${uiState.totalScenarioCount}",
            modifier = Modifier.padding(top = 10.dp)
        )

        SummaryRow(
            label = "Average score",
            value = if (uiState.totalAttempts == 0) {
                "Not available"
            } else {
                "${uiState.averageScore} / 100"
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
            text = "${uiState.completionPercent}% of scenarios attempted",
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
                    "Adaptive Practice Recommendation"
                } else {
                    "Start Your First Practice"
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = "${scenario.category.displayName()} • ${scenario.difficulty.displayName()}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = if (hasAttemptHistory) {
                    "Recommended from your lowest-scoring practised category and least-practised scenario."
                } else {
                    "Begin with an easy scenario to learn the four-step decision process."
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
                Text(text = "Start Recommended Practice")
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
