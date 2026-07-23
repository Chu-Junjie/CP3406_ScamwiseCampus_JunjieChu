package com.chujunjie.scamwisecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chujunjie.scamwisecampus.ui.ScamWiseApp
import com.chujunjie.scamwisecampus.ui.theme.ScamWiseCampusTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.ui.theme.AppThemeViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appThemeViewModel: AppThemeViewModel =
                koinViewModel()

            val themeState by appThemeViewModel.uiState
                .collectAsStateWithLifecycle()

            val useDarkTheme = when (themeState.themeMode) {
                ThemeMode.SYSTEM ->
                    isSystemInDarkTheme()

                ThemeMode.LIGHT ->
                    false

                ThemeMode.DARK ->
                    true
            }

            ScamWiseCampusTheme(
                darkTheme = useDarkTheme
            ) {
                ScamWiseApp()
            }
        }
    }
}