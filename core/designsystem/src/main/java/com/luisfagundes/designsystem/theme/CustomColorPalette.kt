package com.luisfagundes.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorPalette(
    val activeGreen: Color = Color.Unspecified
)

val lightCustomColorPalette = CustomColorPalette(
    activeGreen = lightGreen
)

val darkCustomColorPalette = CustomColorPalette(
    activeGreen = darkGreen
)

val LocalCustomColorPalette = staticCompositionLocalOf { CustomColorPalette() }

val MaterialTheme.customColorPalette: CustomColorPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorPalette.current