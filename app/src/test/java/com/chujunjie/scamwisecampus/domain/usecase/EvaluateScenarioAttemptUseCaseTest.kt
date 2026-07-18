package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.PracticeSubmission
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluateScenarioAttemptUseCaseTest {

    private val evaluateScenarioAttempt =
        EvaluateScenarioAttemptUseCase()

    private val highRiskScenario =
        ScenarioSeedData.scenarios.first { scenario ->
            scenario.id == "job_easy_01"
        }

    private val noClearThreatScenario =
        ScenarioSeedData.scenarios.first { scenario ->
            scenario.id == "job_hard_01"
        }

    @Test
    fun `perfect answer returns one hundred points`() {
        val actualWarningIds = highRiskScenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .map { warningSign -> warningSign.id }
            .toSet()

        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = actualWarningIds,
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.VERY_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )

        assertEquals(40, result.riskScore)
        assertEquals(30, result.warningSignScore)
        assertEquals(30, result.safeActionScore)
        assertEquals(100, result.totalScore)
        assertTrue(result.isRiskCorrect)
        assertTrue(result.isSafeActionCorrect)
        assertEquals(
            ConfidenceCalibration.WELL_CALIBRATED,
            result.confidenceCalibration
        )
    }

    @Test
    fun `correct low confidence answer is underconfident`() {
        val actualWarningIds = highRiskScenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .map { warningSign -> warningSign.id }
            .toSet()

        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = actualWarningIds,
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.NOT_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )

        assertEquals(100, result.totalScore)
        assertEquals(
            ConfidenceCalibration.UNDERCONFIDENT,
            result.confidenceCalibration
        )
    }

    @Test
    fun `incorrect high confidence answer is overconfident`() {
        val actualWarningIds = highRiskScenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .map { warningSign -> warningSign.id }
            .toSet()

        val unsafeActionId = highRiskScenario.actionOptions
            .first { action -> !action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            selectedWarningSignIds = actualWarningIds,
            selectedActionId = unsafeActionId,
            confidenceLevel = ConfidenceLevel.VERY_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )

        assertEquals(30, result.totalScore)
        assertFalse(result.isRiskCorrect)
        assertFalse(result.isSafeActionCorrect)
        assertEquals(
            ConfidenceCalibration.OVERCONFIDENT,
            result.confidenceCalibration
        )
    }

    @Test
    fun `partial warning selection receives proportional score`() {
        val selectedWarningIds = highRiskScenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .take(2)
            .map { warningSign -> warningSign.id }
            .toSet()

        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = selectedWarningIds,
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.SOMEWHAT_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )

        assertEquals(20, result.warningSignScore)
        assertEquals(90, result.totalScore)
    }

    @Test
    fun `incorrect warning selection reduces warning score`() {
        val actualWarningIds = highRiskScenario.warningSigns
            .filter { warningSign -> warningSign.isActualWarning }
            .map { warningSign -> warningSign.id }

        val incorrectWarningId = highRiskScenario.warningSigns
            .first { warningSign -> !warningSign.isActualWarning }
            .id

        val selectedIds =
            (actualWarningIds + incorrectWarningId).toSet()

        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = selectedIds,
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.SOMEWHAT_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )

        assertEquals(20, result.warningSignScore)
        assertEquals(90, result.totalScore)
    }

    @Test
    fun `selecting no warnings in no clear threat scenario receives full warning score`() {
        val safeActionId = noClearThreatScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            selectedWarningSignIds = emptySet(),
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.VERY_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = noClearThreatScenario,
            submission = submission
        )

        assertEquals(30, result.warningSignScore)
        assertEquals(100, result.totalScore)
    }

    @Test
    fun `selecting false warning in no clear threat scenario applies penalty`() {
        val falseWarningId =
            noClearThreatScenario.warningSigns.first().id

        val safeActionId = noClearThreatScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            selectedWarningSignIds = setOf(falseWarningId),
            selectedActionId = safeActionId,
            confidenceLevel = ConfidenceLevel.SOMEWHAT_CONFIDENT
        )

        val result = evaluateScenarioAttempt(
            scenario = noClearThreatScenario,
            submission = submission
        )

        assertEquals(20, result.warningSignScore)
        assertEquals(90, result.totalScore)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `unknown action identifier is rejected`() {
        val submission = PracticeSubmission(
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = emptySet(),
            selectedActionId = "unknown_action",
            confidenceLevel = ConfidenceLevel.NOT_CONFIDENT
        )

        evaluateScenarioAttempt(
            scenario = highRiskScenario,
            submission = submission
        )
    }
}