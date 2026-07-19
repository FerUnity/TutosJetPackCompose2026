package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SaharaColorScheme = lightColorScheme(
    primary = SaharaSienna,
    onPrimary = SaharaWhite,
    primaryContainer = SaharaSiennaLight,
    onPrimaryContainer = SaharaSiennaDark,
    secondary = SaharaGray,
    onSecondary = SaharaWhite,
    tertiary = SaharaRose,
    background = SaharaLinen,
    onBackground = SaharaCharcoal,
    surface = SaharaLinen,
    onSurface = SaharaCharcoal,
    surfaceVariant = SaharaCard,
    onSurfaceVariant = SaharaCharcoal,
    outline = SaharaOutline
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // We enforce the warm, sun-baked Sahara theme as it is the core identity of the brand
    MaterialTheme(
        colorScheme = SaharaColorScheme,
        typography = Typography,
        content = content
    )
}
