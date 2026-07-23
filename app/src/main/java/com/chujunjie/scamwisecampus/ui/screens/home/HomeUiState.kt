package com.chujunjie.scamwisecampus.ui.screens.home

import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario

data class HomeUiState(
    val totalAttempts: Int = 0,
    val completedScenarioCount: Int = 0,
    val totalScenarioCount: Int = 0,
    val averageScore: Int = 0,
    val latestAttempt: AttemptRecord? = null,
    val recommendedCategory: ScamCategory? = null,
    val recommendedScenario: Scenario? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val completionPercent: Int
        get() {
            if (totalScenarioCount == 0) {
                return 0
            }

            return (
                completedScenarioCount.toDouble() /
                    totalScenarioCount *
                    100
                ).toInt()
                .coerceIn(0, 100)
        }
}
