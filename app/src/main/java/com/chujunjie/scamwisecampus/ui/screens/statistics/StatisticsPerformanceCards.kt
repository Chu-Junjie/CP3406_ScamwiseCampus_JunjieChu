package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord

@Composable
internal fun CategoryPerformanceCard(
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
            value =
                performance.attemptCount.toString()
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
            percent =
                performance.riskAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )

        ProgressMetric(
            label = stringResource(
                R.string
                    .statistics_safe_action_accuracy_short
            ),
            percent =
                performance.safeActionAccuracyPercent,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
internal fun RecentAttemptCard(
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
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.score_out_of_100,
                        attempt.totalScore
                    ),
                    style =
                        MaterialTheme.typography.titleMedium,
                    color =
                        MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(
                    attempt.difficulty.displayNameRes()
                ),
                style =
                    MaterialTheme.typography.labelLarge,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = stringResource(
                    attempt.confidenceCalibration
                        .displayNameRes()
                ),
                style =
                    MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}