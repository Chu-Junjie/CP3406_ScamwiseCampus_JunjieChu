package com.chujunjie.scamwisecampus.domain.model

data class AttemptRecord(
    val attemptId: Long = 0,
    val scenarioId: String,
    val category: ScamCategory,
    val difficulty: Difficulty,
    val selectedRiskLevel: RiskLevel,
    val correctRiskLevel: RiskLevel,
    val selectedWarningSignIds: Set<String>,
    val selectedNoWarningSigns: Boolean,
    val selectedActionId: String,
    val confidenceLevel: ConfidenceLevel,
    val riskScore: Int,
    val warningSignScore: Int,
    val safeActionScore: Int,
    val totalScore: Int,
    val isRiskCorrect: Boolean,
    val isSafeActionCorrect: Boolean,
    val confidenceCalibration: ConfidenceCalibration,
    val completedAtEpochMillis: Long
)
