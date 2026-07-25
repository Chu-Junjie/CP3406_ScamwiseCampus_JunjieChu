package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.WarningSign

@Composable
internal fun ResultHeader(
    scenarioTitle: String
) {
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
        text = scenarioTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
internal fun ResultSummaryCard(
    totalScore: Int,
    calibration: ConfidenceCalibration,
    selectedConfidenceLevel: ConfidenceLevel
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.score_out_of_100,
                    totalScore
                ),
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = stringResource(
                    calibration.displayNameRes()
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = stringResource(
                    calibration.descriptionRes()
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

@Composable
internal fun ResultSaveFailureCard(
    saveStatus: ScenarioSaveStatus
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.scenario_save_failed_title
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = stringResource(
                    saveStatus.messageRes()
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
internal fun ResultScoreBreakdownCard(
    evaluation: AttemptEvaluation
) {
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

@Composable
internal fun ResultRiskAssessmentCard(
    selectedRiskLevel: RiskLevel,
    correctRiskLevel: RiskLevel,
    isCorrect: Boolean
) {
    ResultCard(
        title = stringResource(
            R.string.result_risk_assessment
        )
    ) {
        FeedbackLabel(
            isCorrect = isCorrect
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
                    correctRiskLevel.displayNameRes()
                )
            ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
internal fun ResultWarningSignsCard(
    actualWarningSigns: List<WarningSign>,
    correctlySelectedWarningSigns: List<WarningSign>,
    incorrectlySelectedWarningSigns: List<WarningSign>,
    missedWarningSigns: List<WarningSign>,
    selectedNoWarningSigns: Boolean
) {
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
                selectedNoWarningSigns
        )
    }
}

@Composable
internal fun ResultSafeActionCard(
    selectedAction: ActionOption,
    safeAction: ActionOption,
    selectedActionFeedback: String,
    isCorrect: Boolean
) {
    ResultCard(
        title = stringResource(
            R.string.result_safest_action
        )
    ) {
        FeedbackLabel(
            isCorrect = isCorrect
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
            text = selectedActionFeedback,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (!isCorrect) {
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
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
internal fun ResultExplanationCard(
    explanation: String
) {
    ResultCard(
        title = stringResource(
            R.string.result_why_this_matters
        )
    ) {
        Text(
            text = explanation,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
internal fun ResultVerificationAdviceCard(
    verificationAdvice: String
) {
    ResultCard(
        title = stringResource(
            R.string.result_safe_verification_advice
        )
    ) {
        Text(
            text = verificationAdvice,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
internal fun ResultNavigationButtons(
    onRestart: () -> Unit,
    onViewStatistics: () -> Unit,
    onReturnHome: () -> Unit
) {
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
