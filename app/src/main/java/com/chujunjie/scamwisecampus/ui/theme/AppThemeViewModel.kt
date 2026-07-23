package com.chujunjie.scamwisecampus.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AppThemeUiState())

    val uiState: StateFlow<AppThemeUiState> =
        _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.observeThemeMode()
                .collect { themeMode ->
                    _uiState.value = AppThemeUiState(
                        themeMode = themeMode,
                        isLoading = false
                    )
                }
        }
    }
}
