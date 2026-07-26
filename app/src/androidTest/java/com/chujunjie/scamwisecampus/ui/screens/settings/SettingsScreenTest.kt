package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_displaysLoadingMessage() {
        setSettingsContent(
            uiState = SettingsUiState(
                isLoading = true
            )
        )

        composeTestRule
            .onNodeWithText(
                string(R.string.settings_loading)
            )
            .assertIsDisplayed()
    }

    @Test
    fun darkThemeOption_invokesThemeCallback() {
        var selectedThemeMode: ThemeMode? = null

        setSettingsContent(
            uiState = SettingsUiState(
                themeMode = ThemeMode.SYSTEM,
                isLoading = false
            ),
            onThemeModeSelected = { themeMode ->
                selectedThemeMode = themeMode
            }
        )

        composeTestRule
            .onNodeWithText(
                string(R.string.theme_mode_dark)
            )
            .assertIsDisplayed()
            .performClick()

        assertEquals(
            ThemeMode.DARK,
            selectedThemeMode
        )
    }

    @Test
    fun clearHistoryButton_opensConfirmationDialog() {
        setSettingsContent(
            uiState = SettingsUiState(
                isLoading = false
            )
        )

        openClearHistoryDialog()

        composeTestRule
            .onNodeWithText(
                string(
                    R.string
                        .settings_clear_history_dialog_title
                )
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                string(
                    R.string
                        .settings_clear_history_dialog_message
                )
            )
            .assertIsDisplayed()
    }

    @Test
    fun confirmClearHistory_invokesCallbackAndClosesDialog() {
        var clearHistoryCalled = false

        setSettingsContent(
            uiState = SettingsUiState(
                isLoading = false
            ),
            onClearHistory = {
                clearHistoryCalled = true
            }
        )

        openClearHistoryDialog()

        composeTestRule
            .onNodeWithText(
                string(
                    R.string
                        .settings_clear_history_confirm
                )
            )
            .assertIsDisplayed()
            .performClick()

        assertTrue(clearHistoryCalled)

        composeTestRule
            .onAllNodesWithText(
                string(
                    R.string
                        .settings_clear_history_dialog_title
                )
            )
            .assertCountEquals(0)
    }

    @Test
    fun cancelClearHistory_closesDialogWithoutCallback() {
        var clearHistoryCalled = false

        setSettingsContent(
            uiState = SettingsUiState(
                isLoading = false
            ),
            onClearHistory = {
                clearHistoryCalled = true
            }
        )

        openClearHistoryDialog()

        composeTestRule
            .onNodeWithText(
                string(R.string.common_cancel)
            )
            .assertIsDisplayed()
            .performClick()

        assertFalse(clearHistoryCalled)

        composeTestRule
            .onAllNodesWithText(
                string(
                    R.string
                        .settings_clear_history_dialog_title
                )
            )
            .assertCountEquals(0)
    }

    @Test
    fun clearingState_disablesClearHistoryButton() {
        setSettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                isClearingHistory = true
            )
        )

        composeTestRule
            .onNodeWithText(
                string(R.string.settings_clearing)
            )
            .performScrollTo()
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    private fun openClearHistoryDialog() {
        composeTestRule
            .onNodeWithText(
                string(
                    R.string
                        .settings_clear_practice_history
                )
            )
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
    }

    private fun setSettingsContent(
        uiState: SettingsUiState,
        onThemeModeSelected: (ThemeMode) -> Unit = {},
        onClearHistory: () -> Unit = {},
        onDismissFeedback: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            MaterialTheme {
                SettingsScreen(
                    uiState = uiState,
                    onThemeModeSelected =
                        onThemeModeSelected,
                    onClearHistory = onClearHistory,
                    onDismissFeedback =
                        onDismissFeedback
                )
            }
        }
    }

    private fun string(
        @StringRes resourceId: Int
    ): String {
        return InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(resourceId)
    }
}