package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PracticeViewModel(
    private val scenarioRepository: ScenarioRepository
) : ViewModel() {

    private val selectedCategory =
        MutableStateFlow<ScamCategory?>(null)

    private val selectedDifficulty =
        MutableStateFlow<Difficulty?>(null)

    private val _uiState =
        MutableStateFlow(PracticeUiState())

    val uiState: StateFlow<PracticeUiState> =
        _uiState.asStateFlow()

    init {
        observePracticeFilters()
    }

    fun selectCategory(category: ScamCategory?) {
        selectedCategory.value = category
    }

    fun selectDifficulty(difficulty: Difficulty?) {
        selectedDifficulty.value = difficulty
    }

    fun clearFilters() {
        selectedCategory.value = null
        selectedDifficulty.value = null
    }

    private fun observePracticeFilters() {
        viewModelScope.launch {
            combine(
                scenarioRepository.observeScenarios(),
                selectedCategory,
                selectedDifficulty
            ) { scenarios, category, difficulty ->
                val filteredScenarios = scenarios.filter { scenario ->
                    val matchesCategory =
                        category == null || scenario.category == category

                    val matchesDifficulty =
                        difficulty == null || scenario.difficulty == difficulty

                    matchesCategory && matchesDifficulty
                }

                PracticeUiState(
                    scenarios = filteredScenarios,
                    selectedCategory = category,
                    selectedDifficulty = difficulty,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}