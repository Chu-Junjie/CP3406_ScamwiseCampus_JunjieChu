package com.chujunjie.scamwisecampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ScamWisePrimaryLight,
    onPrimary = ScamWiseOnPrimaryLight,
    primaryContainer = ScamWisePrimaryContainerLight,
    onPrimaryContainer = ScamWiseOnPrimaryContainerLight,

    secondary = ScamWiseSecondaryLight,
    onSecondary = ScamWiseOnSecondaryLight,
    secondaryContainer = ScamWiseSecondaryContainerLight,
    onSecondaryContainer = ScamWiseOnSecondaryContainerLight,

    tertiary = ScamWiseTertiaryLight,
    onTertiary = ScamWiseOnTertiaryLight,
    tertiaryContainer = ScamWiseTertiaryContainerLight,
    onTertiaryContainer = ScamWiseOnTertiaryContainerLight,

    background = ScamWiseBackgroundLight,
    onBackground = ScamWiseOnBackgroundLight,
    surface = ScamWiseSurfaceLight,
    onSurface = ScamWiseOnSurfaceLight,
    surfaceVariant = ScamWiseSurfaceVariantLight,
    onSurfaceVariant = ScamWiseOnSurfaceVariantLight,
    outline = ScamWiseOutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = ScamWisePrimaryDark,
    onPrimary = ScamWiseOnPrimaryDark,
    primaryContainer = ScamWisePrimaryContainerDark,
    onPrimaryContainer = ScamWiseOnPrimaryContainerDark,

    secondary = ScamWiseSecondaryDark,
    onSecondary = ScamWiseOnSecondaryDark,
    secondaryContainer = ScamWiseSecondaryContainerDark,
    onSecondaryContainer = ScamWiseOnSecondaryContainerDark,

    tertiary = ScamWiseTertiaryDark,
    onTertiary = ScamWiseOnTertiaryDark,
    tertiaryContainer = ScamWiseTertiaryContainerDark,
    onTertiaryContainer = ScamWiseOnTertiaryContainerDark,

    background = ScamWiseBackgroundDark,
    onBackground = ScamWiseOnBackgroundDark,
    surface = ScamWiseSurfaceDark,
    onSurface = ScamWiseOnSurfaceDark,
    surfaceVariant = ScamWiseSurfaceVariantDark,
    onSurfaceVariant = ScamWiseOnSurfaceVariantDark,
    outline = ScamWiseOutlineDark
)

@Composable
fun ScamWiseCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        typography = Typography,
        shapes = ScamWiseShapes,
        content = content
    )
}