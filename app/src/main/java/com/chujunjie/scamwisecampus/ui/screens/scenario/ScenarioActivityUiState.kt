package com.chujunjie.scamwisecampus.ui.screens.scenario

import com.chujunjie.scamwisecampus.domain.model.AttemptEvaluation
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario

data class ScenarioActivityUiState(
    val scenario: Scenario? = null,
    val currentStep: ScenarioStep = ScenarioStep.RISK_ASSESSMENT,
    val selectedRiskLevel: RiskLevel? = null,
    val selectedWarningSignIds: Set<String> = emptySet(),
    val hasSelectedNoWarningSigns: Boolean = false,
    val selectedActionId: String? = null,
    val selectedConfidenceLevel: ConfidenceLevel? = null,
    val validationMessage: String? = null,
    val evaluation: AttemptEvaluation? = null,
    val isLoading: Boolean = true,
    val isScenarioMissing: Boolean = false
)