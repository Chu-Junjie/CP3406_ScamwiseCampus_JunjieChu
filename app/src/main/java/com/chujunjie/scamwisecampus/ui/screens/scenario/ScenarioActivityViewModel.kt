package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.PracticeSubmission
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScenarioActivityViewModel(
    scenarioId: String,
    private val scenarioRepository: ScenarioRepository,
    private val evaluateScenarioAttempt: EvaluateScenarioAttemptUseCase,
    private val attemptRepository: AttemptRepository
) : ViewModel() {

    private val scenario =
        scenarioRepository.getScenarioById(scenarioId)

    private val _uiState = MutableStateFlow(
        if (scenario == null) {
            ScenarioActivityUiState(
                isLoading = false,
                isScenarioMissing = true
            )
        } else {
            ScenarioActivityUiState(
                scenario = scenario,
                isLoading = false
            )
        }
    )

    val uiState: StateFlow<ScenarioActivityUiState> =
        _uiState.asStateFlow()

    fun selectRiskLevel(riskLevel: RiskLevel) {
        _uiState.update { state ->
            state.copy(
                selectedRiskLevel = riskLevel,
                validationError = null
            )
        }
    }

    fun toggleWarningSign(warningSignId: String) {
        _uiState.update { state ->
            val updatedIds =
                if (warningSignId in state.selectedWarningSignIds) {
                    state.selectedWarningSignIds - warningSignId
                } else {
                    state.selectedWarningSignIds + warningSignId
                }

            state.copy(
                selectedWarningSignIds = updatedIds,
                hasSelectedNoWarningSigns = false,
                validationError = null
            )
        }
    }

    fun selectNoWarningSigns() {
        _uiState.update { state ->
            state.copy(
                selectedWarningSignIds = emptySet(),
                hasSelectedNoWarningSigns =
                    !state.hasSelectedNoWarningSigns,
                validationError = null
            )
        }
    }

    fun selectAction(actionId: String) {
        _uiState.update { state ->
            state.copy(
                selectedActionId = actionId,
                validationError = null
            )
        }
    }

    fun selectConfidence(confidenceLevel: ConfidenceLevel) {
        _uiState.update { state ->
            state.copy(
                selectedConfidenceLevel = confidenceLevel,
                validationError = null
            )
        }
    }

    fun continueToNextStep() {
        val state = _uiState.value

        val validationError = when (state.currentStep) {
            ScenarioStep.RISK_ASSESSMENT -> {
                if (state.selectedRiskLevel == null) {
                    ScenarioValidationError.RISK_LEVEL_REQUIRED
                } else {
                    null
                }
            }

            ScenarioStep.WARNING_SIGNS -> {
                if (
                    state.selectedWarningSignIds.isEmpty() &&
                    !state.hasSelectedNoWarningSigns
                ) {
                    ScenarioValidationError.WARNING_SIGN_REQUIRED
                } else {
                    null
                }
            }

            ScenarioStep.SAFE_ACTION -> {
                if (state.selectedActionId == null) {
                    ScenarioValidationError.ACTION_REQUIRED
                } else {
                    null
                }
            }

            ScenarioStep.CONFIDENCE -> null
        }

        if (validationError != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    validationError = validationError
                )
            }

            return
        }

        val nextStep = when (state.currentStep) {
            ScenarioStep.RISK_ASSESSMENT ->
                ScenarioStep.WARNING_SIGNS

            ScenarioStep.WARNING_SIGNS ->
                ScenarioStep.SAFE_ACTION

            ScenarioStep.SAFE_ACTION ->
                ScenarioStep.CONFIDENCE

            ScenarioStep.CONFIDENCE ->
                ScenarioStep.CONFIDENCE
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentStep = nextStep,
                validationError = null
            )
        }
    }

    fun moveToPreviousStep(): Boolean {
        val state = _uiState.value

        val previousStep = when (state.currentStep) {
            ScenarioStep.RISK_ASSESSMENT -> null

            ScenarioStep.WARNING_SIGNS ->
                ScenarioStep.RISK_ASSESSMENT

            ScenarioStep.SAFE_ACTION ->
                ScenarioStep.WARNING_SIGNS

            ScenarioStep.CONFIDENCE ->
                ScenarioStep.SAFE_ACTION
        }

        if (previousStep == null) {
            return false
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentStep = previousStep,
                validationError = null
            )
        }

        return true
    }

    fun submitAttempt() {
        val state = _uiState.value

        if (state.evaluation != null || state.isSavingAttempt) {
            return
        }

        val currentScenario = state.scenario ?: return
        val selectedConfidence = state.selectedConfidenceLevel

        if (selectedConfidence == null) {
            _uiState.update { currentState ->
                currentState.copy(
                    validationError =
                        ScenarioValidationError.CONFIDENCE_REQUIRED
                )
            }

            return
        }

        val submission = PracticeSubmission(
            selectedRiskLevel =
                requireNotNull(state.selectedRiskLevel),
            selectedWarningSignIds =
                state.selectedWarningSignIds,
            selectedActionId =
                requireNotNull(state.selectedActionId),
            confidenceLevel =
                selectedConfidence
        )

        val evaluation = evaluateScenarioAttempt(
            scenario = currentScenario,
            submission = submission
        )

        val attemptRecord = createAttemptRecord(
            scenario = currentScenario,
            state = state,
            evaluation = evaluation
        )

        _uiState.update { currentState ->
            currentState.copy(
                evaluation = evaluation,
                validationError = null,
                isSavingAttempt = true,
                isAttemptSaved = false,
                saveStatus = null
            )
        }

        saveAttempt(
            attemptRecord = attemptRecord,
            evaluation = evaluation
        )
    }

    fun restartScenario() {
        val currentScenario = scenario ?: return

        _uiState.value = ScenarioActivityUiState(
            scenario = currentScenario,
            isLoading = false
        )
    }

    private fun saveAttempt(
        attemptRecord: AttemptRecord,
        evaluation: AttemptEvaluation
    ) {
        viewModelScope.launch {
            runCatching {
                attemptRepository.saveAttempt(attemptRecord)
            }.onSuccess {
                updateSaveState(
                    evaluation = evaluation,
                    isSaved = true,
                    saveStatus = null
                )
            }.onFailure {
                updateSaveState(
                    evaluation = evaluation,
                    isSaved = false,
                    saveStatus =
                        ScenarioSaveStatus.SAVE_FAILED
                )
            }
        }
    }

    private fun updateSaveState(
        evaluation: AttemptEvaluation,
        isSaved: Boolean,
        saveStatus: ScenarioSaveStatus?
    ) {
        _uiState.update { currentState ->
            if (currentState.evaluation != evaluation) {
                currentState
            } else {
                currentState.copy(
                    isSavingAttempt = false,
                    isAttemptSaved = isSaved,
                    saveStatus = saveStatus
                )
            }
        }
    }

    private fun createAttemptRecord(
        scenario: Scenario,
        state: ScenarioActivityUiState,
        evaluation: AttemptEvaluation
    ): AttemptRecord {
        return AttemptRecord(
            scenarioId = scenario.id,
            category = scenario.category,
            difficulty = scenario.difficulty,
            selectedRiskLevel =
                requireNotNull(state.selectedRiskLevel),
            correctRiskLevel = scenario.correctRiskLevel,
            selectedWarningSignIds =
                state.selectedWarningSignIds,
            selectedNoWarningSigns =
                state.hasSelectedNoWarningSigns,
            selectedActionId =
                requireNotNull(state.selectedActionId),
            confidenceLevel =
                requireNotNull(state.selectedConfidenceLevel),
            riskScore = evaluation.riskScore,
            warningSignScore =
                evaluation.warningSignScore,
            safeActionScore =
                evaluation.safeActionScore,
            totalScore = evaluation.totalScore,
            isRiskCorrect = evaluation.isRiskCorrect,
            isSafeActionCorrect =
                evaluation.isSafeActionCorrect,
            confidenceCalibration =
                evaluation.confidenceCalibration,
            completedAtEpochMillis =
                System.currentTimeMillis()
        )
    }
}
