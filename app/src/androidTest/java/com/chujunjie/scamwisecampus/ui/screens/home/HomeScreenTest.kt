package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun linkVerificationButton_opensLinkVerificationLab() {
        var linkVerificationClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        totalScenarioCount = 18,
                        isLoading = false
                    ),
                    onScenarioClick = {},
                    onPracticeClick = {},
                    onStatisticsClick = {},
                    onLinkVerificationClick = {
                        linkVerificationClicked = true
                    }
                )
            }
        }

        composeTestRule
            .onNodeWithText(
                "Open Link Verification Lab"
            )
            .assertIsDisplayed()
            .performClick()

        assertTrue(linkVerificationClicked)
    }
}
