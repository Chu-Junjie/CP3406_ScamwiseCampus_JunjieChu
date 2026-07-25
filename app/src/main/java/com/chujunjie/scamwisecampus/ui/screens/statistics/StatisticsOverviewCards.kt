package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

@Composable
internal fun SummaryCard(
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
internal fun RecommendationCard(
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
                    R.string
                        .statistics_recommended_practice
                ),
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(
                    category.displayNameRes()
                ),
                style =
                    MaterialTheme.typography.headlineSmall,
                color =
                    MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = stringResource(
                    R.string
                        .statistics_recommendation_description
                ),
                style =
                    MaterialTheme.typography.bodyMedium,
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
internal fun AccuracyCard(
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
                R.string
                    .statistics_risk_assessment_accuracy
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
internal fun CalibrationCard(
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
            count =
                uiState.cautiousButIncorrectCount
        )
    }
}