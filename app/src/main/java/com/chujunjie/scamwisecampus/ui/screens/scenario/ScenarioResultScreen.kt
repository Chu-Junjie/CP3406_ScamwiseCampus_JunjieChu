package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario

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
    saveStatus: ScenarioSaveStatus? = null,
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

    val actualWarningSigns =
        scenario.warningSigns.filter { warningSign ->
            warningSign.isActualWarning
        }

    val selectedWarningSigns =
        scenario.warningSigns.filter { warningSign ->
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
            ResultHeader(
                scenarioTitle = scenario.title
            )
        }

        item {
            ResultSummaryCard(
                totalScore = evaluation.totalScore,
                calibration =
                    evaluation.confidenceCalibration,
                selectedConfidenceLevel =
                    selectedConfidenceLevel
            )
        }

        if (saveStatus != null) {
            item {
                ResultSaveFailureCard(
                    saveStatus = saveStatus
                )
            }
        }

        item {
            ResultScoreBreakdownCard(
                evaluation = evaluation
            )
        }

        item {
            ResultRiskAssessmentCard(
                selectedRiskLevel = selectedRiskLevel,
                correctRiskLevel =
                    scenario.correctRiskLevel,
                isCorrect = evaluation.isRiskCorrect
            )
        }

        item {
            ResultWarningSignsCard(
                actualWarningSigns =
                    actualWarningSigns,
                correctlySelectedWarningSigns =
                    correctlySelectedWarningSigns,
                incorrectlySelectedWarningSigns =
                    incorrectlySelectedWarningSigns,
                missedWarningSigns =
                    missedWarningSigns,
                selectedNoWarningSigns =
                    hasSelectedNoWarningSigns
            )
        }

        item {
            ResultSafeActionCard(
                selectedAction = selectedAction,
                safeAction = safeAction,
                selectedActionFeedback =
                    evaluation.selectedActionFeedback,
                isCorrect =
                    evaluation.isSafeActionCorrect
            )
        }

        item {
            ResultExplanationCard(
                explanation = scenario.explanation
            )
        }

        item {
            ResultVerificationAdviceCard(
                verificationAdvice =
                    scenario.verificationAdvice
            )
        }

        item {
            ResultNavigationButtons(
                onRestart = onRestart,
                onViewStatistics = onViewStatistics,
                onReturnHome = onReturnHome
            )
        }
    }
}
