package com.chujunjie.scamwisecampus.ui.screens.statistics

import com.chujunjie.scamwisecampus.domain.model.ScamCategory

data class CategoryPerformance(
    val category: ScamCategory,
    val attemptCount: Int,
    val averageScore: Int,
    val highestScore: Int,
    val riskAccuracyPercent: Int,
    val safeActionAccuracyPercent: Int
)
