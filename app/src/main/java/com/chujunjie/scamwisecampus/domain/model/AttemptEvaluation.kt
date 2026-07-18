package com.chujunjie.scamwisecampus.domain.model

data class AttemptEvaluation(
    val riskScore: Int,
    val warningSignScore: Int,
    val safeActionScore: Int,
    val totalScore: Int,
    val isRiskCorrect: Boolean,
    val isSafeActionCorrect: Boolean,
    val confidenceCalibration: ConfidenceCalibration,
    val selectedActionFeedback: String
)