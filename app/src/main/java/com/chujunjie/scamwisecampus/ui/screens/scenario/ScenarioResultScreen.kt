package com.chujunjie.scamwisecampus.ui.screens.scenario

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
import androidx.compose.ui.unit.dp
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
                text = "Practice Complete",
                style = MaterialTheme.typography.headlineMedium
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
                        text = "${evaluation.totalScore} / 100",
                        style = MaterialTheme.typography.displaySmall
                    )

                    Text(
                        text = evaluation.confidenceCalibration.displayName(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = evaluation.confidenceCalibration.description(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Reported confidence: ${selectedConfidenceLevel.displayName()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        item {
            ResultCard(
                title = "Score Breakdown"
            ) {
                ScoreRow(
                    label = "Risk assessment",
                    score = evaluation.riskScore,
                    maximumScore = 40
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ScoreRow(
                    label = "Warning signs",
                    score = evaluation.warningSignScore,
                    maximumScore = 30
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                ScoreRow(
                    label = "Safe action",
                    score = evaluation.safeActionScore,
                    maximumScore = 30
                )
            }
        }

        item {
            ResultCard(
                title = "Risk Assessment"
            ) {
                FeedbackLabel(
                    isCorrect = evaluation.isRiskCorrect
                )

                Text(
                    text = "Your answer: ${selectedRiskLevel.displayName()}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = "Correct answer: ${scenario.correctRiskLevel.displayName()}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        item {
            ResultCard(
                title = "Warning Signs"
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
                title = "Safest Action"
            ) {
                FeedbackLabel(
                    isCorrect = evaluation.isSafeActionCorrect
                )

                Text(
                    text = "Your answer:",
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
                        text = "Recommended action:",
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
                title = "Why This Matters"
            ) {
                Text(
                    text = scenario.explanation,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            ResultCard(
                title = "Safe Verification Advice"
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
                Text(text = "Try Again")
            }

            OutlinedButton(
                onClick = onViewStatistics,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(text = "View Statistics")
            }

            TextButton(
                onClick = onReturnHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(text = "Return Home")
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
            text = "$score / $maximumScore",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun FeedbackLabel(
    isCorrect: Boolean
) {
    Text(
        text = if (isCorrect) {
            "Correct"
        } else {
            "Needs Review"
        },
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
                text = "You correctly identified that the message contains no clear warning signs.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        actualWarningSigns.isEmpty() -> {
            FeedbackLabel(isCorrect = false)

            Text(
                text = "This scenario contains no clear warning signs.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )

            FeedbackList(
                heading = "Indicators incorrectly selected:",
                warningSigns = incorrectlySelectedWarningSigns
            )
        }

        else -> {
            if (correctlySelectedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = "Correctly identified:",
                    warningSigns = correctlySelectedWarningSigns
                )
            }

            if (missedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = "Missed warning signs:",
                    warningSigns = missedWarningSigns
                )
            }

            if (incorrectlySelectedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = "Incorrectly selected:",
                    warningSigns = incorrectlySelectedWarningSigns
                )
            }

            if (
                correctlySelectedWarningSigns.size ==
                actualWarningSigns.size &&
                incorrectlySelectedWarningSigns.isEmpty()
            ) {
                Text(
                    text = "You identified all warning signs correctly.",
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
            text = "• ${warningSign.description}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 6.dp
            )
        )
    }
}

private fun RiskLevel.displayName(): String {
    return when (this) {
        RiskLevel.HIGH_RISK ->
            "High Risk"

        RiskLevel.NEEDS_VERIFICATION ->
            "Needs Verification"

        RiskLevel.NO_CLEAR_THREAT ->
            "No Clear Threat Identified"
    }
}

private fun ConfidenceLevel.displayName(): String {
    return when (this) {
        ConfidenceLevel.NOT_CONFIDENT ->
            "Not Confident"

        ConfidenceLevel.SOMEWHAT_CONFIDENT ->
            "Somewhat Confident"

        ConfidenceLevel.VERY_CONFIDENT ->
            "Very Confident"
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

private fun ConfidenceCalibration.description(): String {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            "Your confidence matched a strong answer."

        ConfidenceCalibration.UNDERCONFIDENT ->
            "Your answer was strong, but you may be underestimating your judgement."

        ConfidenceCalibration.OVERCONFIDENT ->
            "Your confidence was high, but important parts of the answer need review."

        ConfidenceCalibration.NEEDS_REVIEW ->
            "Review the warning signs and safer response before trying another scenario."

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            "You recognised uncertainty, but the answer still needs further review."
    }
}