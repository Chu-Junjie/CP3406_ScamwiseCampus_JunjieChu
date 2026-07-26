package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.annotation.StringRes
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ThemeMode

@StringRes
internal fun ThemeMode.displayNameRes(): Int {
    return when (this) {
        ThemeMode.SYSTEM ->
            R.string.theme_mode_system

        ThemeMode.LIGHT ->
            R.string.theme_mode_light

        ThemeMode.DARK ->
            R.string.theme_mode_dark
    }
}

@StringRes
internal fun ThemeMode.descriptionRes(): Int {
    return when (this) {
        ThemeMode.SYSTEM ->
            R.string.theme_mode_system_description

        ThemeMode.LIGHT ->
            R.string.theme_mode_light_description

        ThemeMode.DARK ->
            R.string.theme_mode_dark_description
    }
}

@StringRes
internal fun SettingsFeedback.messageRes(): Int {
    return when (this) {
        SettingsFeedback.THEME_SAVE_FAILED ->
            R.string.settings_theme_save_failed

        SettingsFeedback.HISTORY_CLEARED ->
            R.string.settings_history_cleared

        SettingsFeedback.HISTORY_CLEAR_FAILED ->
            R.string.settings_history_clear_failed
    }
}