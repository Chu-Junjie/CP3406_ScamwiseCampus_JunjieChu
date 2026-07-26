package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysDashboardMessage() {
        setHomeContent(
            uiState = HomeUiState(
                isLoading = true
            )
        )

        composeTestRule
            .onNodeWithText(
                "Loading your learning dashboard..."
            )
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysReadableMessage() {
        setHomeContent(
            uiState = HomeUiState(
                isLoading = false,
                error = HomeStatusMessage.LOAD_FAILED
            )
        )

        composeTestRule
            .onNodeWithText(
                "Home information could not be loaded."
            )
            .assertIsDisplayed()
    }

    @Test
    fun emptyDashboard_displaysZeroProgress() {
        setHomeContent(
            uiState = HomeUiState(
                totalScenarioCount = 18,
                isLoading = false
            )
        )

        composeTestRule
            .onNodeWithText("Learning Progress")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Not available")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "0% of scenarios attempted"
            )
            .assertIsDisplayed()
    }

    @Test
    fun recommendation_displaysCorrectSeparatorAndOpensScenario() {
        val scenario =
            ScenarioSeedData.scenarios.first()

        var selectedScenarioId: String? = null

        setHomeContent(
            uiState = HomeUiState(
                totalScenarioCount =
                    ScenarioSeedData.scenarios.size,
                recommendedScenario = scenario,
                isLoading = false
            ),
            onScenarioClick = { scenarioId ->
                selectedScenarioId = scenarioId
            }
        )

        val context =
            InstrumentationRegistry
                .getInstrumentation()
                .targetContext

        val categoryDifficultyText =
            context.getString(
                R.string
                    .scenario_category_difficulty_format,
                "Job",
                "Easy"
            )

        composeTestRule
            .onNodeWithText(scenario.title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(categoryDifficultyText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "Start Recommended Practice"
            )
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertEquals(
            scenario.id,
            selectedScenarioId
        )
    }

    @Test
    fun browseScenariosButton_invokesPracticeCallback() {
        var practiceClicked = false

        setHomeContent(
            uiState = HomeUiState(
                totalScenarioCount = 18,
                isLoading = false
            ),
            onPracticeClick = {
                practiceClicked = true
            }
        )

        composeTestRule
            .onNodeWithText("Browse All Scenarios")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertTrue(practiceClicked)
    }

    @Test
    fun statisticsButton_invokesStatisticsCallback() {
        var statisticsClicked = false

        setHomeContent(
            uiState = HomeUiState(
                totalScenarioCount = 18,
                isLoading = false
            ),
            onStatisticsClick = {
                statisticsClicked = true
            }
        )

        composeTestRule
            .onNodeWithText("View Statistics")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertTrue(statisticsClicked)
    }

    @Test
    fun linkVerificationButton_invokesLabCallback() {
        var linkVerificationClicked = false

        setHomeContent(
            uiState = HomeUiState(
                totalScenarioCount = 18,
                isLoading = false
            ),
            onLinkVerificationClick = {
                linkVerificationClicked = true
            }
        )

        composeTestRule
            .onNodeWithText(
                "Open Link Verification Lab"
            )
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertTrue(linkVerificationClicked)
    }

    private fun setHomeContent(
        uiState: HomeUiState,
        onScenarioClick: (String) -> Unit = {},
        onPracticeClick: () -> Unit = {},
        onStatisticsClick: () -> Unit = {},
        onLinkVerificationClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            MaterialTheme {
                HomeScreen(
                    uiState = uiState,
                    onScenarioClick = onScenarioClick,
                    onPracticeClick = onPracticeClick,
                    onStatisticsClick =
                        onStatisticsClick,
                    onLinkVerificationClick =
                        onLinkVerificationClick
                )
            }
        }
    }
}