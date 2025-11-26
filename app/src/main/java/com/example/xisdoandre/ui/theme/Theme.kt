package com.example.xisdoandre.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============ TEMA PERSONALIZADO ============
private val XisDoAndreColorScheme = lightColorScheme(
    primary = Color(0xFFF6202A),
    onPrimary = Color.White,
    secondary = Color(0xFFC97C75),
    onSecondary = Color.White,
    tertiary = Color(0xFFEABD66),
    onTertiary = Color.Black,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun XisDoAndreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = XisDoAndreColorScheme,
        typography = Typography,
        content = content
    )
}