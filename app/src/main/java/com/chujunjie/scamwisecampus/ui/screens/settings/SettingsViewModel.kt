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
                    feedbackMessage = null
                )
            }

            runCatching {
                settingsRepository.setThemeMode(themeMode)
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        feedbackMessage =
                            "Theme preference could not be saved."
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
                    feedbackMessage = null
                )
            }

            runCatching {
                attemptRepository.clearAttempts()
            }.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isClearingHistory = false,
                        feedbackMessage =
                            "Practice history cleared."
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isClearingHistory = false,
                        feedbackMessage =
                            "Practice history could not be cleared."
                    )
                }
            }
        }
    }

    fun dismissFeedback() {
        _uiState.update { state ->
            state.copy(
                feedbackMessage = null
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
