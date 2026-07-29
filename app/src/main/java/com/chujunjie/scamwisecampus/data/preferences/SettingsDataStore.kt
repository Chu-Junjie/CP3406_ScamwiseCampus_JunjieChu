package com.chujunjie.scamwisecampus.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_DATA_STORE_NAME =
    "scamwise_settings"

val Context.settingsDataStore: DataStore<Preferences> by
    preferencesDataStore(
        name = SETTINGS_DATA_STORE_NAME
    )
