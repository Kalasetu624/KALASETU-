package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KalaSetuLightColorScheme = lightColorScheme(
    primary = PastelBluePrimary,
    onPrimary = Color.White,
    primaryContainer = PastelBlueContainer,
    onPrimaryContainer = PastelBlueDark,
    secondary = OrderDoneGreen,
    onSecondary = Color.White,
    secondaryContainer = OrderDoneGreenContainer,
    onSecondaryContainer = OrderDoneGreenText,
    tertiary = PastelBlueAccent,
    onTertiary = InkDark,
    background = PureWhite,
    onBackground = InkDark,
    surface = PureWhite,
    onSurface = InkDark,
    surfaceVariant = PastelBlueLight,
    onSurfaceVariant = InkMedium,
    outline = PastelBlueBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KalaSetuLightColorScheme,
        typography = Typography,
        content = content
    )
}
