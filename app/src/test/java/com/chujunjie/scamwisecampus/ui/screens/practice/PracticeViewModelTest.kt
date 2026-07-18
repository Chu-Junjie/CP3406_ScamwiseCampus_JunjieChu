package com.chujunjie.scamwisecampus.ui.screens.practice

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class PracticeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository =
        FakeScenarioRepository(ScenarioSeedData.scenarios)

    @Test
    fun `initial state contains all scenarios`() {
        val viewModel = PracticeViewModel(repository)

        assertEquals(
            18,
            viewModel.uiState.value.scenarios.size
        )

        assertFalse(
            viewModel.uiState.value.isLoading
        )
    }

    @Test
    fun `selecting category filters scenarios`() {
        val viewModel = PracticeViewModel(repository)

        viewModel.selectCategory(ScamCategory.BANKING)

        val scenarios = viewModel.uiState.value.scenarios

        assertEquals(3, scenarios.size)

        assertEquals(
            true,
            scenarios.all { scenario ->
                scenario.category == ScamCategory.BANKING
            }
        )
    }

    @Test
    fun `selecting difficulty filters scenarios`() {
        val viewModel = PracticeViewModel(repository)

        viewModel.selectDifficulty(Difficulty.EASY)

        val scenarios = viewModel.uiState.value.scenarios

        assertEquals(6, scenarios.size)

        assertEquals(
            true,
            scenarios.all { scenario ->
                scenario.difficulty == Difficulty.EASY
            }
        )
    }

    @Test
    fun `category and difficulty filters work together`() {
        val viewModel = PracticeViewModel(repository)

        viewModel.selectCategory(ScamCategory.PARCEL)
        viewModel.selectDifficulty(Difficulty.MEDIUM)

        val scenarios = viewModel.uiState.value.scenarios

        assertEquals(1, scenarios.size)
        assertEquals(
            "parcel_medium_01",
            scenarios.first().id
        )
    }

    @Test
    fun `clearing filters restores all scenarios`() {
        val viewModel = PracticeViewModel(repository)

        viewModel.selectCategory(ScamCategory.JOB)
        viewModel.selectDifficulty(Difficulty.HARD)
        viewModel.clearFilters()

        assertEquals(
            18,
            viewModel.uiState.value.scenarios.size
        )
    }

    private class FakeScenarioRepository(
        private val scenarios: List<Scenario>
    ) : ScenarioRepository {

        override fun observeScenarios(): Flow<List<Scenario>> {
            return flowOf(scenarios)
        }

        override fun getScenarioById(scenarioId: String): Scenario? {
            return scenarios.firstOrNull { scenario ->
                scenario.id == scenarioId
            }
        }
    }
}