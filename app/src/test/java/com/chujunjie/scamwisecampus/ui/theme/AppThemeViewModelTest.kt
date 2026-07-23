package com.chujunjie.scamwisecampus.ui.theme

import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class AppThemeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `theme changes are exposed to app`() {
        val repository =
            FakeSettingsRepository()

        val viewModel =
            AppThemeViewModel(repository)

        repository.updateTheme(
            ThemeMode.DARK
        )

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(
            ThemeMode.DARK,
            state.themeMode
        )
    }

    private class FakeSettingsRepository :
        SettingsRepository {

        private val themeMode =
            MutableStateFlow(ThemeMode.SYSTEM)

        override fun observeThemeMode():
            Flow<ThemeMode> {
            return themeMode
        }

        override suspend fun setThemeMode(
            themeMode: ThemeMode
        ) {
            this.themeMode.value = themeMode
        }

        fun updateTheme(
            themeMode: ThemeMode
        ) {
            this.themeMode.value = themeMode
        }
    }
}
