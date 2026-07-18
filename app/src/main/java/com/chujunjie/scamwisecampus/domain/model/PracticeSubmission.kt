package com.chujunjie.scamwisecampus.domain.model

data class PracticeSubmission(
    val selectedRiskLevel: RiskLevel,
    val selectedWarningSignIds: Set<String>,
    val selectedActionId: String,
    val confidenceLevel: ConfidenceLevel
)