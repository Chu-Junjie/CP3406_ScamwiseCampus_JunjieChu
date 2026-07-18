package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.PracticeSubmission
import com.chujunjie.scamwisecampus.domain.model.Scenario
import kotlin.math.roundToInt

class EvaluateScenarioAttemptUseCase {

    operator fun invoke(
        scenario: Scenario,
        submission: PracticeSubmission
    ): AttemptEvaluation {
        validateSubmission(
            scenario = scenario,
            submission = submission
        )

        val isRiskCorrect =
            submission.selectedRiskLevel == scenario.correctRiskLevel

        val selectedAction = scenario.actionOptions.first { action ->
            action.id == submission.selectedActionId
        }

        val riskScore = if (isRiskCorrect) {
            RISK_SCORE_MAXIMUM
        } else {
            0
        }

        val warningSignScore = calculateWarningSignScore(
            scenario = scenario,
            selectedWarningSignIds = submission.selectedWarningSignIds
        )

        val safeActionScore = if (selectedAction.isSafeAction) {
            SAFE_ACTION_SCORE_MAXIMUM
        } else {
            0
        }

        val totalScore =
            riskScore + warningSignScore + safeActionScore

        return AttemptEvaluation(
            riskScore = riskScore,
            warningSignScore = warningSignScore,
            safeActionScore = safeActionScore,
            totalScore = totalScore,
            isRiskCorrect = isRiskCorrect,
            isSafeActionCorrect = selectedAction.isSafeAction,
            confidenceCalibration = classifyConfidence(
                totalScore = totalScore,
                confidenceLevel = submission.confidenceLevel
            ),
            selectedActionFeedback = selectedAction.feedback
        )
    }

    private fun validateSubmission(
        scenario: Scenario,
        submission: PracticeSubmission
    ) {
        val validWarningSignIds = scenario.warningSigns
            .map { warningSign -> warningSign.id }
            .toSet()

        require(
            submission.selectedWarningSignIds.all { selectedId ->
                selectedId in validWarningSignIds
            }
        ) {
            "The submission contains a warning sign that does not belong to the scenario."
        }

        require(
            scenario.actionOptions.any { action ->
                action.id == submission.selectedActionId
            }
        ) {
            "The selected action does not belong to the scenario."
        }
    }

    private fun calculateWarningSignScore(
        scenario: Scenario,
        selectedWarningSignIds: Set<String>
    ): Int {
        val actualWarningSignIds = scenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .map { warningSign -> warningSign.id }
            .toSet()

        if (actualWarningSignIds.isEmpty()) {
            return calculateNoThreatWarningScore(
                selectedWarningSignIds = selectedWarningSignIds
            )
        }

        val correctSelectionCount =
            selectedWarningSignIds.count { selectedId ->
                selectedId in actualWarningSignIds
            }

        val incorrectSelectionCount =
            selectedWarningSignIds.count { selectedId ->
                selectedId !in actualWarningSignIds
            }

        val adjustedCorrectCount =
            correctSelectionCount - incorrectSelectionCount

        val scoreRatio =
            adjustedCorrectCount.toDouble() / actualWarningSignIds.size

        return (scoreRatio * WARNING_SIGN_SCORE_MAXIMUM)
            .roundToInt()
            .coerceIn(
                minimumValue = 0,
                maximumValue = WARNING_SIGN_SCORE_MAXIMUM
            )
    }

    private fun calculateNoThreatWarningScore(
        selectedWarningSignIds: Set<String>
    ): Int {
        val penalty =
            selectedWarningSignIds.size * NO_THREAT_SELECTION_PENALTY

        return (WARNING_SIGN_SCORE_MAXIMUM - penalty)
            .coerceAtLeast(0)
    }

    private fun classifyConfidence(
        totalScore: Int,
        confidenceLevel: ConfidenceLevel
    ): ConfidenceCalibration {
        val isCorrectAttempt =
            totalScore >= CORRECT_ATTEMPT_THRESHOLD

        return if (isCorrectAttempt) {
            when (confidenceLevel) {
                ConfidenceLevel.NOT_CONFIDENT ->
                    ConfidenceCalibration.UNDERCONFIDENT

                ConfidenceLevel.SOMEWHAT_CONFIDENT,
                ConfidenceLevel.VERY_CONFIDENT ->
                    ConfidenceCalibration.WELL_CALIBRATED
            }
        } else {
            when (confidenceLevel) {
                ConfidenceLevel.VERY_CONFIDENT ->
                    ConfidenceCalibration.OVERCONFIDENT

                ConfidenceLevel.SOMEWHAT_CONFIDENT ->
                    ConfidenceCalibration.NEEDS_REVIEW

                ConfidenceLevel.NOT_CONFIDENT ->
                    ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT
            }
        }
    }

    private companion object {
        const val RISK_SCORE_MAXIMUM = 40
        const val WARNING_SIGN_SCORE_MAXIMUM = 30
        const val SAFE_ACTION_SCORE_MAXIMUM = 30
        const val CORRECT_ATTEMPT_THRESHOLD = 80
        const val NO_THREAT_SELECTION_PENALTY = 10
    }
}