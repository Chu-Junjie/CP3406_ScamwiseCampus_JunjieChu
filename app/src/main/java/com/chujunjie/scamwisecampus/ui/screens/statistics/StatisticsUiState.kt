package com.chujunjie.scamwisecampus.ui.screens.statistics

import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

data class StatisticsUiState(
    val totalAttempts: Int = 0,
    val averageScore: Int = 0,
    val highestScore: Int = 0,
    val riskAccuracyPercent: Int = 0,
    val safeActionAccuracyPercent: Int = 0,
    val wellCalibratedCount: Int = 0,
    val underconfidentCount: Int = 0,
    val overconfidentCount: Int = 0,
    val needsReviewCount: Int = 0,
    val cautiousButIncorrectCount: Int = 0,
    val categoryPerformance: List<CategoryPerformance> = emptyList(),
    val recommendedCategory: ScamCategory? = null,
    val recentAttempts: List<AttemptRecord> = emptyList(),
    val isLoading: Boolean = true,
    val error: StatisticsStatusMessage? = null
) {
    val hasAttempts: Boolean
        get() = totalAttempts > 0
}

enum class StatisticsStatusMessage {
    LOAD_FAILED
}
