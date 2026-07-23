package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LinkVerificationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noKnownThreatResult_showsQualifiedSafetyWarning() {
        var clearClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                LinkVerificationScreen(
                    uiState = LinkVerificationUiState(
                        urlInput = "https://example.com",
                        hasConsent = true,
                        result = LinkVerificationResult
                            .NoKnownThreat(
                                checkedUrl =
                                    "https://example.com",
                                domain = "example.com"
                            )
                    ),
                    onUrlInputChanged = {},
                    onConsentChanged = {},
                    onCheckUrl = {},
                    onClearResult = {
                        clearClicked = true
                    },
                    onNavigateBack = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(
                "No Known Threat Match Found"
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "This does not prove that the page is safe. Continue to verify the sender and purpose independently."
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Check Another URL")
            .performClick()

        assertTrue(clearClicked)
    }
}
