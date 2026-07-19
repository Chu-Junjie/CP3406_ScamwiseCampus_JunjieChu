package com.chujunjie.scamwisecampus.ui.screens.scenario

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ScenarioActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scenarios = ScenarioSeedData.scenarios

    private val scenarioRepository = FakeScenarioRepository(
        scenarios = scenarios
    )

    private val attemptRepository = FakeAttemptRepository()

    private val evaluateScenarioAttempt =
        EvaluateScenarioAttemptUseCase()

    private val highRiskScenario = scenarios.first { scenario ->
        scenario.id == HIGH_RISK_SCENARIO_ID
    }

    @Test
    fun `valid scenario identifier loads scenario`() {
        val state = createViewModel().uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isScenarioMissing)
        assertEquals(
            HIGH_RISK_SCENARIO_ID,
            state.scenario?.id
        )
    }

    @Test
    fun `unknown scenario identifier displays missing state`() {
        val state = createViewModel(
            scenarioId = "unknown_scenario"
        ).uiState.value

        assertTrue(state.isScenarioMissing)
        assertNull(state.scenario)
    }

    @Test
    fun `continuing without risk selection displays validation message`() {
        val viewModel = createViewModel()

        viewModel.continueToNextStep()

        assertEquals(
            "Select a risk level to continue.",
            viewModel.uiState.value.validationMessage
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
    fun `perfect submission returns one hundred points and is saved`() {
        val viewModel = createViewModel()

        completePerfectAttempt(viewModel)

        val state = viewModel.uiState.value
        val savedAttempt =
            attemptRepository.savedAttempts.single()

        assertEquals(100, state.evaluation?.totalScore)
        assertEquals(
            ConfidenceCalibration.WELL_CALIBRATED,
            state.evaluation?.confidenceCalibration
        )
        assertEquals(
            HIGH_RISK_SCENARIO_ID,
            savedAttempt.scenarioId
        )
        assertEquals(100, savedAttempt.totalScore)
        assertTrue(savedAttempt.completedAtEpochMillis > 0)
        assertTrue(state.isAttemptSaved)
        assertFalse(state.isSavingAttempt)
        assertNull(state.saveErrorMessage)
    }

    @Test
    fun `submitting completed attempt twice saves only once`() {
        val viewModel = createViewModel()

        completePerfectAttempt(viewModel)
        viewModel.submitAttempt()

        assertEquals(
            1,
            attemptRepository.savedAttempts.size
        )
    }

    @Test
    fun `save failure preserves evaluation and reports error`() {
        val failingRepository = FakeAttemptRepository(
            shouldFail = true
        )

        val viewModel = createViewModel(
            repository = failingRepository
        )

        completePerfectAttempt(viewModel)

        val state = viewModel.uiState.value

        assertEquals(100, state.evaluation?.totalScore)
        assertFalse(state.isSavingAttempt)
        assertFalse(state.isAttemptSaved)
        assertEquals(
            "Your result is available, but this attempt could not be saved.",
            state.saveErrorMessage
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
    }

    @Test
    fun `restarting scenario clears answers and save state`() {
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
        assertNull(state.selectedActionId)
        assertNull(state.selectedConfidenceLevel)
        assertNull(state.evaluation)
        assertFalse(state.isSavingAttempt)
        assertFalse(state.isAttemptSaved)
        assertNull(state.saveErrorMessage)
    }

    private fun createViewModel(
        scenarioId: String = HIGH_RISK_SCENARIO_ID,
        repository: AttemptRepository = attemptRepository
    ): ScenarioActivityViewModel {
        return ScenarioActivityViewModel(
            scenarioId = scenarioId,
            scenarioRepository = scenarioRepository,
            evaluateScenarioAttempt =
                evaluateScenarioAttempt,
            attemptRepository = repository
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
            return MutableStateFlow(scenarios)
        }

        override fun getScenarioById(
            scenarioId: String
        ): Scenario? {
            return scenarios.firstOrNull { scenario ->
                scenario.id == scenarioId
            }
        }
    }

    private class FakeAttemptRepository(
        private val shouldFail: Boolean = false
    ) : AttemptRepository {

        private val attemptsFlow =
            MutableStateFlow<List<AttemptRecord>>(
                emptyList()
            )

        val savedAttempts: List<AttemptRecord>
            get() = attemptsFlow.value

        override fun observeAttempts():
                Flow<List<AttemptRecord>> {
            return attemptsFlow
        }

        override suspend fun saveAttempt(
            attemptRecord: AttemptRecord
        ): Long {
            if (shouldFail) {
                error("Simulated database failure.")
            }

            val attemptId =
                attemptsFlow.value.size.toLong() + 1

            attemptsFlow.value =
                attemptsFlow.value +
                        attemptRecord.copy(
                            attemptId = attemptId
                        )

            return attemptId
        }

        override suspend fun clearAttempts() {
            attemptsFlow.value = emptyList()
        }
    }

    private companion object {
        const val HIGH_RISK_SCENARIO_ID =
            "job_easy_01"
    }
}
