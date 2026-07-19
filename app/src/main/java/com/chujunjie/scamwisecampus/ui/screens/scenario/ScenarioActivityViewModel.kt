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
                validationMessage = null
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
                validationMessage = null
            )
        }
    }

    fun selectNoWarningSigns() {
        _uiState.update { state ->
            state.copy(
                selectedWarningSignIds = emptySet(),
                hasSelectedNoWarningSigns =
                    !state.hasSelectedNoWarningSigns,
                validationMessage = null
            )
        }
    }

    fun selectAction(actionId: String) {
        _uiState.update { state ->
            state.copy(
                selectedActionId = actionId,
                validationMessage = null
            )
        }
    }

    fun selectConfidence(confidenceLevel: ConfidenceLevel) {
        _uiState.update { state ->
            state.copy(
                selectedConfidenceLevel = confidenceLevel,
                validationMessage = null
            )
        }
    }

    fun continueToNextStep() {
        val state = _uiState.value

        val validationMessage = when (state.currentStep) {
            ScenarioStep.RISK_ASSESSMENT -> {
                if (state.selectedRiskLevel == null) {
                    "Select a risk level to continue."
                } else {
                    null
                }
            }

            ScenarioStep.WARNING_SIGNS -> {
                if (
                    state.selectedWarningSignIds.isEmpty() &&
                    !state.hasSelectedNoWarningSigns
                ) {
                    "Select at least one warning sign or choose no clear warning signs."
                } else {
                    null
                }
            }

            ScenarioStep.SAFE_ACTION -> {
                if (state.selectedActionId == null) {
                    "Select an action to continue."
                } else {
                    null
                }
            }

            ScenarioStep.CONFIDENCE -> null
        }

        if (validationMessage != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    validationMessage = validationMessage
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
                validationMessage = null
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
                validationMessage = null
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
                    validationMessage =
                        "Select your confidence level before submitting."
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
                validationMessage = null,
                isSavingAttempt = true,
                isAttemptSaved = false,
                saveErrorMessage = null
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
                    errorMessage = null
                )
            }.onFailure {
                updateSaveState(
                    evaluation = evaluation,
                    isSaved = false,
                    errorMessage =
                        "Your result is available, but this attempt could not be saved."
                )
            }
        }
    }

    private fun updateSaveState(
        evaluation: AttemptEvaluation,
        isSaved: Boolean,
        errorMessage: String?
    ) {
        _uiState.update { currentState ->
            if (currentState.evaluation != evaluation) {
                currentState
            } else {
                currentState.copy(
                    isSavingAttempt = false,
                    isAttemptSaved = isSaved,
                    saveErrorMessage = errorMessage
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
