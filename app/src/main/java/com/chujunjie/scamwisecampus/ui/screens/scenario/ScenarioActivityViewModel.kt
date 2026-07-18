package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.lifecycle.ViewModel
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.PracticeSubmission
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScenarioActivityViewModel(
    scenarioId: String,
    private val scenarioRepository: ScenarioRepository,
    private val evaluateScenarioAttempt: EvaluateScenarioAttemptUseCase
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

        _uiState.update { currentState ->
            currentState.copy(
                evaluation = evaluation,
                validationMessage = null
            )
        }
    }

    fun restartScenario() {
        val currentScenario = scenario ?: return

        _uiState.value = ScenarioActivityUiState(
            scenario = currentScenario,
            isLoading = false
        )
    }
}