package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyHistory_showsStartPracticeAction() {
        var practiceClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                StatisticsScreen(
                    uiState = StatisticsUiState(
                        isLoading = false
                    ),
                    onPracticeClick = {
                        practiceClicked = true
                    }
                )
            }
        }

        composeTestRule
            .onNodeWithText("No practice history yet")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Start Practice")
            .assertIsDisplayed()
            .performClick()

        assertTrue(practiceClicked)
    }
}
