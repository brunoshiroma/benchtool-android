package com.brunoshiroma.benchtool_android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0AA330),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF077338),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFB50707),
    onSecondary = Color.White,
)

@Composable
fun BenchtoolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
