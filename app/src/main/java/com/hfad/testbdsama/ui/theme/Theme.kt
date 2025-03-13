package com.hfad.testbdsama.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF12D954), // Зеленый для темной темы
    secondary = Color(0xFF12D954),
    surface = LiteGray, // Темно-серый для поверхностей
    background = gray, // Черный для фона
    onBackground = Color.Black, // Белый текст на темном фоне
    onSurface = Color.White, // Белый текст на поверхностях
    primaryContainer = LiteGrayMedium,

)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF12D954), // Зеленый для светлой темы
    secondary = Color(0xFF12D954),
    surface = LiteGray2, // Темно-серый для поверхностей
    background = Color.White, //  для фона
    onBackground = Color.Black, // Белый текст на темном фоне
    onSurface = Color.Black, // Белый текст на поверхностях
    primaryContainer = LiteGrayMedium2,
)

@Composable
fun TestBDsamaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}