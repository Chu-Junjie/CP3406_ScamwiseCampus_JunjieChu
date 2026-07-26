package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PracticeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysLoadingMessage() {
        setPracticeContent(
            uiState = PracticeUiState(
                isLoading = true
            )
        )

        composeTestRule
            .onNodeWithText(
                "Loading practice scenarios..."
            )
            .assertIsDisplayed()
    }

    @Test
    fun categoryFilter_invokesCategoryCallback() {
        var selectedCategory: ScamCategory? = null

        setPracticeContent(
            uiState = PracticeUiState(
                scenarios = emptyList(),
                isLoading = false
            ),
            onCategorySelected = { category ->
                selectedCategory = category
            }
        )

        composeTestRule
            .onNodeWithText("Banking")
            .assertIsDisplayed()
            .performClick()

        assertEquals(
            ScamCategory.BANKING,
            selectedCategory
        )
    }

    @Test
    fun difficultyFilter_invokesDifficultyCallback() {
        var selectedDifficulty: Difficulty? = null

        setPracticeContent(
            uiState = PracticeUiState(
                scenarios = emptyList(),
                isLoading = false
            ),
            onDifficultySelected = { difficulty ->
                selectedDifficulty = difficulty
            }
        )

        composeTestRule
            .onNodeWithText("Medium")
            .assertIsDisplayed()
            .performClick()

        assertEquals(
            Difficulty.MEDIUM,
            selectedDifficulty
        )
    }

    @Test
    fun emptyResult_clearFiltersInvokesCallback() {
        var clearFiltersClicked = false

        setPracticeContent(
            uiState = PracticeUiState(
                scenarios = emptyList(),
                isLoading = false
            ),
            onClearFilters = {
                clearFiltersClicked = true
            }
        )

        composeTestRule
            .onNodeWithText(
                "No scenarios match the selected filters."
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Clear Filters")
            .assertIsDisplayed()
            .performClick()

        assertTrue(clearFiltersClicked)
    }

    @Test
    fun scenarioCard_displaysDetailsAndInvokesNavigation() {
        val scenario =
            ScenarioSeedData.scenarios.first()

        var selectedScenarioId: String? = null

        setPracticeContent(
            uiState = PracticeUiState(
                scenarios = listOf(scenario),
                isLoading = false
            ),
            onScenarioClick = { scenarioId ->
                selectedScenarioId = scenarioId
            }
        )

        composeTestRule
            .onNodeWithText(scenario.title)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithText(
                "From: ${scenario.sender}"
            )
            .assertIsDisplayed()

        assertEquals(
            scenario.id,
            selectedScenarioId
        )
    }

    private fun setPracticeContent(
        uiState: PracticeUiState,
        onCategorySelected: (ScamCategory?) -> Unit = {},
        onDifficultySelected: (Difficulty?) -> Unit = {},
        onClearFilters: () -> Unit = {},
        onScenarioClick: (String) -> Unit = {}
    ) {
        composeTestRule.setContent {
            MaterialTheme {
                PracticeScreen(
                    uiState = uiState,
                    onCategorySelected =
                        onCategorySelected,
                    onDifficultySelected =
                        onDifficultySelected,
                    onClearFilters = onClearFilters,
                    onScenarioClick = onScenarioClick
                )
            }
        }
    }
}