package com.chujunjie.scamwisecampus.ui.screens.scenario

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScenarioActivityViewModelTest {

    private val scenarios = ScenarioSeedData.scenarios

    private val repository = FakeScenarioRepository(
        scenarios = scenarios
    )

    private val evaluateScenarioAttempt =
        EvaluateScenarioAttemptUseCase()

    private val highRiskScenario = scenarios.first { scenario ->
        scenario.id == HIGH_RISK_SCENARIO_ID
    }

    @Test
    fun `valid scenario identifier loads scenario`() {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isScenarioMissing)
        assertEquals(
            HIGH_RISK_SCENARIO_ID,
            state.scenario?.id
        )
        assertEquals(
            ScenarioStep.RISK_ASSESSMENT,
            state.currentStep
        )
    }

    @Test
    fun `unknown scenario identifier displays missing state`() {
        val viewModel = createViewModel(
            scenarioId = "unknown_scenario"
        )

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertTrue(state.isScenarioMissing)
        assertNull(state.scenario)
    }

    @Test
    fun `continuing without risk selection displays validation message`() {
        val viewModel = createViewModel()

        viewModel.continueToNextStep()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.RISK_ASSESSMENT,
            state.currentStep
        )
        assertEquals(
            "Select a risk level to continue.",
            state.validationMessage
        )
    }

    @Test
    fun `selecting risk level allows warning sign step`() {
        val viewModel = createViewModel()

        viewModel.selectRiskLevel(
            highRiskScenario.correctRiskLevel
        )
        viewModel.continueToNextStep()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.WARNING_SIGNS,
            state.currentStep
        )
        assertNull(state.validationMessage)
    }

    @Test
    fun `continuing without warning selection displays validation message`() {
        val viewModel = createViewModel()

        moveToWarningStep(viewModel)

        viewModel.continueToNextStep()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.WARNING_SIGNS,
            state.currentStep
        )
        assertEquals(
            "Select at least one warning sign or choose no clear warning signs.",
            state.validationMessage
        )
    }

    @Test
    fun `warning sign can be selected and removed`() {
        val viewModel = createViewModel()
        val warningSignId =
            highRiskScenario.warningSigns.first().id

        moveToWarningStep(viewModel)

        viewModel.toggleWarningSign(warningSignId)

        assertTrue(
            warningSignId in
                    viewModel.uiState.value.selectedWarningSignIds
        )

        viewModel.toggleWarningSign(warningSignId)

        assertFalse(
            warningSignId in
                    viewModel.uiState.value.selectedWarningSignIds
        )
    }

    @Test
    fun `selecting no warning signs clears warning selections`() {
        val viewModel = createViewModel()
        val warningSignId =
            highRiskScenario.warningSigns.first().id

        moveToWarningStep(viewModel)

        viewModel.toggleWarningSign(warningSignId)
        viewModel.selectNoWarningSigns()

        val state = viewModel.uiState.value

        assertTrue(state.hasSelectedNoWarningSigns)
        assertTrue(state.selectedWarningSignIds.isEmpty())
    }

    @Test
    fun `selecting warning sign clears no warning signs selection`() {
        val viewModel = createViewModel()
        val warningSignId =
            highRiskScenario.warningSigns.first().id

        moveToWarningStep(viewModel)

        viewModel.selectNoWarningSigns()
        viewModel.toggleWarningSign(warningSignId)

        val state = viewModel.uiState.value

        assertFalse(state.hasSelectedNoWarningSigns)
        assertEquals(
            setOf(warningSignId),
            state.selectedWarningSignIds
        )
    }

    @Test
    fun `completed warning selection allows safe action step`() {
        val viewModel = createViewModel()
        val warningSignId =
            highRiskScenario.warningSigns.first().id

        moveToWarningStep(viewModel)

        viewModel.toggleWarningSign(warningSignId)
        viewModel.continueToNextStep()

        assertEquals(
            ScenarioStep.SAFE_ACTION,
            viewModel.uiState.value.currentStep
        )
    }

    @Test
    fun `continuing without action displays validation message`() {
        val viewModel = createViewModel()

        moveToSafeActionStep(viewModel)

        viewModel.continueToNextStep()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.SAFE_ACTION,
            state.currentStep
        )
        assertEquals(
            "Select an action to continue.",
            state.validationMessage
        )
    }

    @Test
    fun `selecting action allows confidence step`() {
        val viewModel = createViewModel()
        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        moveToSafeActionStep(viewModel)

        viewModel.selectAction(safeActionId)
        viewModel.continueToNextStep()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.CONFIDENCE,
            state.currentStep
        )
        assertEquals(
            safeActionId,
            state.selectedActionId
        )
    }

    @Test
    fun `submitting without confidence displays validation message`() {
        val viewModel = createViewModel()

        moveToConfidenceStep(viewModel)

        viewModel.submitAttempt()

        val state = viewModel.uiState.value

        assertNull(state.evaluation)
        assertEquals(
            "Select your confidence level before submitting.",
            state.validationMessage
        )
    }

    @Test
    fun `perfect submission returns one hundred points`() {
        val viewModel = createViewModel()

        completePerfectAttempt(viewModel)

        val evaluation =
            viewModel.uiState.value.evaluation

        assertEquals(
            100,
            evaluation?.totalScore
        )
        assertEquals(
            ConfidenceCalibration.WELL_CALIBRATED,
            evaluation?.confidenceCalibration
        )
    }

    @Test
    fun `back action moves through previous steps`() {
        val viewModel = createViewModel()

        moveToConfidenceStep(viewModel)

        assertTrue(viewModel.moveToPreviousStep())
        assertEquals(
            ScenarioStep.SAFE_ACTION,
            viewModel.uiState.value.currentStep
        )

        assertTrue(viewModel.moveToPreviousStep())
        assertEquals(
            ScenarioStep.WARNING_SIGNS,
            viewModel.uiState.value.currentStep
        )

        assertTrue(viewModel.moveToPreviousStep())
        assertEquals(
            ScenarioStep.RISK_ASSESSMENT,
            viewModel.uiState.value.currentStep
        )

        assertFalse(viewModel.moveToPreviousStep())
        assertEquals(
            ScenarioStep.RISK_ASSESSMENT,
            viewModel.uiState.value.currentStep
        )
    }

    @Test
    fun `restarting scenario clears previous answers`() {
        val viewModel = createViewModel()

        completePerfectAttempt(viewModel)

        viewModel.restartScenario()

        val state = viewModel.uiState.value

        assertEquals(
            ScenarioStep.RISK_ASSESSMENT,
            state.currentStep
        )
        assertNull(state.selectedRiskLevel)
        assertTrue(state.selectedWarningSignIds.isEmpty())
        assertFalse(state.hasSelectedNoWarningSigns)
        assertNull(state.selectedActionId)
        assertNull(state.selectedConfidenceLevel)
        assertNull(state.validationMessage)
        assertNull(state.evaluation)
    }

    private fun createViewModel(
        scenarioId: String = HIGH_RISK_SCENARIO_ID
    ): ScenarioActivityViewModel {
        return ScenarioActivityViewModel(
            scenarioId = scenarioId,
            scenarioRepository = repository,
            evaluateScenarioAttempt =
                evaluateScenarioAttempt
        )
    }

    private fun moveToWarningStep(
        viewModel: ScenarioActivityViewModel
    ) {
        viewModel.selectRiskLevel(
            highRiskScenario.correctRiskLevel
        )
        viewModel.continueToNextStep()
    }

    private fun moveToSafeActionStep(
        viewModel: ScenarioActivityViewModel
    ) {
        moveToWarningStep(viewModel)

        val warningSignId =
            highRiskScenario.warningSigns.first().id

        viewModel.toggleWarningSign(warningSignId)
        viewModel.continueToNextStep()
    }

    private fun moveToConfidenceStep(
        viewModel: ScenarioActivityViewModel
    ) {
        moveToSafeActionStep(viewModel)

        val safeActionId = highRiskScenario.actionOptions
            .first { action -> action.isSafeAction }
            .id

        viewModel.selectAction(safeActionId)
        viewModel.continueToNextStep()
    }

    private fun completePerfectAttempt(
        viewModel: ScenarioActivityViewModel
    ) {
        viewModel.selectRiskLevel(
            highRiskScenario.correctRiskLevel
        )
        viewModel.continueToNextStep()

        highRiskScenario.warningSigns
            .filter { warningSign ->
                warningSign.isActualWarning
            }
            .forEach { warningSign ->
                viewModel.toggleWarningSign(
                    warningSign.id
                )
            }

        viewModel.continueToNextStep()

        val safeActionId = highRiskScenario.actionOptions
            .first { action ->
                action.isSafeAction
            }
            .id

        viewModel.selectAction(safeActionId)
        viewModel.continueToNextStep()

        viewModel.selectConfidence(
            ConfidenceLevel.VERY_CONFIDENT
        )

        viewModel.submitAttempt()
    }

    private class FakeScenarioRepository(
        private val scenarios: List<Scenario>
    ) : ScenarioRepository {

        override fun observeScenarios(): Flow<List<Scenario>> {
            return flowOf(scenarios)
        }

        override fun getScenarioById(
            scenarioId: String
        ): Scenario? {
            return scenarios.firstOrNull { scenario ->
                scenario.id == scenarioId
            }
        }
    }

    private companion object {
        const val HIGH_RISK_SCENARIO_ID =
            "job_easy_01"
    }
}