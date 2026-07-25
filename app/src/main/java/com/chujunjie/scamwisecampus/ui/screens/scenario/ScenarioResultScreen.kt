package com.chujunjie.scamwisecampus.ui.screens.scenario

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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

@Composable
fun ScenarioResultScreen(
    scenario: Scenario,
    selectedRiskLevel: RiskLevel,
    selectedWarningSignIds: Set<String>,
    hasSelectedNoWarningSigns: Boolean,
    selectedActionId: String,
    selectedConfidenceLevel: ConfidenceLevel,
    evaluation: AttemptEvaluation,
    onRestart: () -> Unit,
    onReturnHome: () -> Unit,
    onViewStatistics: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedAction = requireNotNull(
        scenario.actionOptions.firstOrNull { action ->
            action.id == selectedActionId
        }
    )

    val safeAction = requireNotNull(
        scenario.actionOptions.firstOrNull { action ->
            action.isSafeAction
        }
    )

    val actualWarningSigns = scenario.warningSigns.filter { warningSign ->
        warningSign.isActualWarning
    }

    val selectedWarningSigns = scenario.warningSigns.filter { warningSign ->
        warningSign.id in selectedWarningSignIds
    }

    val correctlySelectedWarningSigns =
        selectedWarningSigns.filter { warningSign ->
            warningSign.isActualWarning
        }

    val incorrectlySelectedWarningSigns =
        selectedWarningSigns.filter { warningSign ->
            !warningSign.isActualWarning
        }

    val missedWarningSigns =
        actualWarningSigns.filter { warningSign ->
            warningSign.id !in selectedWarningSignIds
        }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 24.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(
                    R.string.result_practice_complete
                ),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.score_out_of_100,
                            evaluation.totalScore
                        ),
                        style = MaterialTheme.typography.displaySmall
                    )

                    Text(
                        text = stringResource(
                            evaluation.confidenceCalibration
                                .displayNameRes()
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = stringResource(
                            evaluation.confidenceCalibration
                                .descriptionRes()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.result_reported_confidence_format,
                            stringResource(
                                selectedConfidenceLevel.displayNameRes()
                            )
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_score_breakdown
                )
            ) {
                ScoreRow(
                    label = stringResource(
                        R.string.result_risk_assessment_label
                    ),
                    score = evaluation.riskScore,
                    maximumScore = 40
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ScoreRow(
                    label = stringResource(
                        R.string.result_warning_signs_label
                    ),
                    score = evaluation.warningSignScore,
                    maximumScore = 30
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ScoreRow(
                    label = stringResource(
                        R.string.result_safe_action_label
                    ),
                    score = evaluation.safeActionScore,
                    maximumScore = 30
                )
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_risk_assessment
                )
            ) {
                FeedbackLabel(
                    isCorrect = evaluation.isRiskCorrect
                )

                Text(
                    text = stringResource(
                        R.string.result_your_answer_format,
                        stringResource(
                            selectedRiskLevel.displayNameRes()
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = stringResource(
                        R.string.result_correct_answer_format,
                        stringResource(
                            scenario.correctRiskLevel.displayNameRes()
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_warning_signs
                )
            ) {
                WarningSignFeedback(
                    actualWarningSigns = actualWarningSigns,
                    correctlySelectedWarningSigns =
                        correctlySelectedWarningSigns,
                    incorrectlySelectedWarningSigns =
                        incorrectlySelectedWarningSigns,
                    missedWarningSigns = missedWarningSigns,
                    selectedNoWarningSigns =
                        hasSelectedNoWarningSigns
                )
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_safest_action
                )
            ) {
                FeedbackLabel(
                    isCorrect = evaluation.isSafeActionCorrect
                )

                Text(
                    text = stringResource(
                        R.string.result_your_answer_label
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = selectedAction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = evaluation.selectedActionFeedback,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (!evaluation.isSafeActionCorrect) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.result_recommended_action
                        ),
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = safeAction.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = safeAction.feedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_why_this_matters
                )
            ) {
                Text(
                    text = scenario.explanation,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            ResultCard(
                title = stringResource(
                    R.string.result_safe_verification_advice
                )
            ) {
                Text(
                    text = scenario.verificationAdvice,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            Button(
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.result_try_again
                    )
                )
            }

            OutlinedButton(
                onClick = onViewStatistics,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.result_view_statistics
                    )
                )
            }

            TextButton(
                onClick = onReturnHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.result_return_home
                    )
                )
            }
        }
    }
}

@Composable
private fun ResultCard(
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
private fun ScoreRow(
    label: String,
    score: Int,
    maximumScore: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(
                R.string.fraction_format,
                score,
                maximumScore
            ),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun FeedbackLabel(
    isCorrect: Boolean
) {
    Text(
        text = stringResource(
            if (isCorrect) {
                R.string.result_correct
            } else {
                R.string.result_needs_review
            }
        ),
        style = MaterialTheme.typography.labelLarge,
        color = if (isCorrect) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
    )
}

@Composable
private fun WarningSignFeedback(
    actualWarningSigns: List<WarningSign>,
    correctlySelectedWarningSigns: List<WarningSign>,
    incorrectlySelectedWarningSigns: List<WarningSign>,
    missedWarningSigns: List<WarningSign>,
    selectedNoWarningSigns: Boolean
) {
    when {
        actualWarningSigns.isEmpty() &&
                selectedNoWarningSigns -> {
            FeedbackLabel(isCorrect = true)

            Text(
                text = stringResource(
                    R.string.result_warning_none_correct
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        actualWarningSigns.isEmpty() -> {
            FeedbackLabel(isCorrect = false)

            Text(
                text = stringResource(
                    R.string.result_warning_none
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )

            FeedbackList(
                heading = stringResource(
                    R.string
                        .result_indicators_incorrectly_selected
                ),
                warningSigns = incorrectlySelectedWarningSigns
            )
        }

        else -> {
            if (correctlySelectedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_correctly_identified
                    ),
                    warningSigns = correctlySelectedWarningSigns
                )
            }

            if (missedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_missed_warning_signs
                    ),
                    warningSigns = missedWarningSigns
                )
            }

            if (incorrectlySelectedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_incorrectly_selected
                    ),
                    warningSigns = incorrectlySelectedWarningSigns
                )
            }

            if (
                correctlySelectedWarningSigns.size ==
                actualWarningSigns.size &&
                incorrectlySelectedWarningSigns.isEmpty()
            ) {
                Text(
                    text = stringResource(
                        R.string
                            .result_all_warning_signs_correct
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FeedbackList(
    heading: String,
    warningSigns: List<WarningSign>
) {
    if (warningSigns.isEmpty()) {
        return
    }

    Text(
        text = heading,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(top = 12.dp)
    )

    warningSigns.forEach { warningSign ->
        Text(
            text = stringResource(
                R.string.result_bullet_format,
                warningSign.description
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 6.dp
            )
        )
    }
}

@StringRes
private fun RiskLevel.displayNameRes(): Int {
    return when (this) {
        RiskLevel.HIGH_RISK ->
            R.string.risk_level_high

        RiskLevel.NEEDS_VERIFICATION ->
            R.string.risk_level_needs_verification

        RiskLevel.NO_CLEAR_THREAT ->
            R.string.risk_level_no_clear_threat
    }
}

@StringRes
private fun ConfidenceLevel.displayNameRes(): Int {
    return when (this) {
        ConfidenceLevel.NOT_CONFIDENT ->
            R.string.confidence_not_confident

        ConfidenceLevel.SOMEWHAT_CONFIDENT ->
            R.string.confidence_somewhat_confident

        ConfidenceLevel.VERY_CONFIDENT ->
            R.string.confidence_very_confident
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

@StringRes
private fun ConfidenceCalibration.descriptionRes(): Int {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            R.string.calibration_description_well_calibrated

        ConfidenceCalibration.UNDERCONFIDENT ->
            R.string.calibration_description_underconfident

        ConfidenceCalibration.OVERCONFIDENT ->
            R.string.calibration_description_overconfident

        ConfidenceCalibration.NEEDS_REVIEW ->
            R.string.calibration_description_needs_review

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            R.string
                .calibration_description_cautious_but_incorrect
    }
}