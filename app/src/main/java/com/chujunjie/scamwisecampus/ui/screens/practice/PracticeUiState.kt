package com.chujunjie.scamwisecampus.ui.screens.practice

import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario

data class PracticeUiState(
    val scenarios: List<Scenario> = emptyList(),
    val selectedCategory: ScamCategory? = null,
    val selectedDifficulty: Difficulty? = null,
    val isLoading: Boolean = true
)