package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val attemptRepository: AttemptRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> =
        _uiState.asStateFlow()

    init {
        observeSettings()
    }

    fun selectThemeMode(themeMode: ThemeMode) {
        if (themeMode == _uiState.value.themeMode) {
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    feedback = null
                )
            }

            runCatching {
                settingsRepository.setThemeMode(themeMode)
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        feedback =
                            SettingsFeedback.THEME_SAVE_FAILED
                    )
                }
            }
        }
    }

    fun clearPracticeHistory() {
        if (_uiState.value.isClearingHistory) {
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isClearingHistory = true,
                    feedback = null
                )
            }

            runCatching {
                attemptRepository.clearAttempts()
            }.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isClearingHistory = false,
                        feedback =
                            SettingsFeedback.HISTORY_CLEARED
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isClearingHistory = false,
                        feedback =
                            SettingsFeedback.HISTORY_CLEAR_FAILED
                    )
                }
            }
        }
    }

    fun dismissFeedback() {
        _uiState.update { state ->
            state.copy(
                feedback = null
            )
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.observeThemeMode()
                .collect { themeMode ->
                    _uiState.update { state ->
                        state.copy(
                            themeMode = themeMode,
                            isLoading = false
                        )
                    }
                }
        }
    }
}
