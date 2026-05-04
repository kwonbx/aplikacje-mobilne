package com.example.szlaki.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF2B382B),
    surface = Color(0xFF2B382B),
    primary = Color(0xFFBBE8BB),
    secondary = Color(0xFFA4D9A4),
    tertiary = Color(0xFF86CEB0),
    surfaceVariant = Color(0xFF3A4D3A),
    error = Color(0xFFF17F7F),
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFE0FDE0),
    surface = Color(0xFFE0FDE0),
    primary = Color(0xFF335233),
    secondary = Color(0xFF557255),
    tertiary = Color(0xFF527E6B),
    surfaceVariant = Color(0xFFC2E3C2),
    error = Color(0xFFCB5A5A)


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SzlakiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}