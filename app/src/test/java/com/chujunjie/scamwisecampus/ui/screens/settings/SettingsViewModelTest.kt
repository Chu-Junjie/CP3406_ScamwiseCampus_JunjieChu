package com.chujunjie.scamwisecampus.ui.screens.settings

import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `initial theme is loaded from repository`() {
        val settingsRepository =
            FakeSettingsRepository(
                initialThemeMode = ThemeMode.DARK
            )

        val viewModel = SettingsViewModel(
            settingsRepository = settingsRepository,
            attemptRepository =
                FakeAttemptRepository()
        )

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(
            ThemeMode.DARK,
            state.themeMode
        )
    }

    @Test
    fun `selecting theme saves preference`() {
        val settingsRepository =
            FakeSettingsRepository()

        val viewModel = SettingsViewModel(
            settingsRepository = settingsRepository,
            attemptRepository =
                FakeAttemptRepository()
        )

        viewModel.selectThemeMode(
            ThemeMode.LIGHT
        )

        assertEquals(
            ThemeMode.LIGHT,
            settingsRepository.currentThemeMode
        )

        assertEquals(
            ThemeMode.LIGHT,
            viewModel.uiState.value.themeMode
        )
    }

    @Test
    fun `clearing history calls attempt repository`() {
        val attemptRepository =
            FakeAttemptRepository()

        val viewModel = SettingsViewModel(
            settingsRepository =
                FakeSettingsRepository(),
            attemptRepository =
                attemptRepository
        )

        viewModel.clearPracticeHistory()

        assertTrue(
            attemptRepository.wasCleared
        )

        assertEquals(
            "Practice history cleared.",
            viewModel.uiState.value.feedbackMessage
        )
    }

    @Test
    fun `clear history failure displays error`() {
        val attemptRepository =
            FakeAttemptRepository(
                shouldFail = true
            )

        val viewModel = SettingsViewModel(
            settingsRepository =
                FakeSettingsRepository(),
            attemptRepository =
                attemptRepository
        )

        viewModel.clearPracticeHistory()

        assertFalse(
            viewModel.uiState.value.isClearingHistory
        )

        assertEquals(
            "Practice history could not be cleared.",
            viewModel.uiState.value.feedbackMessage
        )
    }

    private class FakeSettingsRepository(
        initialThemeMode: ThemeMode =
            ThemeMode.SYSTEM
    ) : SettingsRepository {

        private val themeMode =
            MutableStateFlow(initialThemeMode)

        val currentThemeMode: ThemeMode
            get() = themeMode.value

        override fun observeThemeMode():
            Flow<ThemeMode> {
            return themeMode
        }

        override suspend fun setThemeMode(
            themeMode: ThemeMode
        ) {
            this.themeMode.value = themeMode
        }
    }

    private class FakeAttemptRepository(
        private val shouldFail: Boolean = false
    ) : AttemptRepository {

        var wasCleared: Boolean = false
            private set

        override fun observeAttempts():
            Flow<List<AttemptRecord>> {
            return MutableStateFlow(emptyList())
        }

        override suspend fun saveAttempt(
            attemptRecord: AttemptRecord
        ): Long {
            return 1
        }

        override suspend fun clearAttempts() {
            if (shouldFail) {
                error("Simulated failure.")
            }

            wasCleared = true
        }
    }
}
