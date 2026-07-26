package com.chujunjie.scamwisecampus.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsRepositoryImplTest {

    @Test
    fun `missing preference returns system theme`() = runTest {
        val dataStore = FakePreferencesDataStore()
        val repository = SettingsRepositoryImpl(dataStore)

        val themeMode = repository.observeThemeMode().first()

        assertEquals(ThemeMode.SYSTEM, themeMode)
    }

    @Test
    fun `saved theme is emitted from preferences`() = runTest {
        val dataStore = FakePreferencesDataStore()
        val repository = SettingsRepositoryImpl(dataStore)

        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(
            ThemeMode.DARK,
            repository.observeThemeMode().first()
        )
        assertEquals(
            ThemeMode.DARK.name,
            dataStore.currentPreferences()[THEME_MODE_KEY]
        )
    }

    @Test
    fun `changing theme replaces previous preference`() = runTest {
        val dataStore = FakePreferencesDataStore()
        val repository = SettingsRepositoryImpl(dataStore)

        repository.setThemeMode(ThemeMode.DARK)
        repository.setThemeMode(ThemeMode.LIGHT)

        assertEquals(
            ThemeMode.LIGHT,
            repository.observeThemeMode().first()
        )
    }

    @Test
    fun `invalid stored value falls back to system theme`() = runTest {
        val dataStore = FakePreferencesDataStore()

        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = "BROKEN_VALUE"
        }

        val repository = SettingsRepositoryImpl(dataStore)

        assertEquals(
            ThemeMode.SYSTEM,
            repository.observeThemeMode().first()
        )
    }

    @Test
    fun `io exception falls back to system theme`() = runTest {
        val repository = SettingsRepositoryImpl(
            ThrowingPreferencesDataStore(
                IOException("Read failed")
            )
        )

        assertEquals(
            ThemeMode.SYSTEM,
            repository.observeThemeMode().first()
        )
    }

    @Test
    fun `non io exception is rethrown`() = runTest {
        val repository = SettingsRepositoryImpl(
            ThrowingPreferencesDataStore(
                IllegalStateException("Unexpected failure")
            )
        )

        var thrown: Throwable? = null

        try {
            repository.observeThemeMode().first()
        } catch (exception: Throwable) {
            thrown = exception
        }

        assertTrue(thrown is IllegalStateException)
        assertEquals("Unexpected failure", thrown?.message)
    }

    private class FakePreferencesDataStore : DataStore<Preferences> {

        private val state = MutableStateFlow(emptyPreferences())

        override val data: Flow<Preferences> = state

        override suspend fun updateData(
            transform: suspend (Preferences) -> Preferences
        ): Preferences {
            val updated = transform(state.value)
            state.value = updated
            return updated
        }

        fun currentPreferences(): Preferences {
            return state.value
        }
    }

    private class ThrowingPreferencesDataStore(
        private val exception: Throwable
    ) : DataStore<Preferences> {

        override val data: Flow<Preferences> = flow {
            throw exception
        }

        override suspend fun updateData(
            transform: suspend (Preferences) -> Preferences
        ): Preferences {
            throw UnsupportedOperationException(
                "updateData is not used in this test"
            )
        }
    }

    private companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}