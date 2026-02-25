package com.chigo.githubusersapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.chigo.githubusersapp.R
import com.chigo.githubusersapp.ui.theme.ThemeMode.Dark
import com.chigo.githubusersapp.ui.theme.ThemeMode.Light
import com.chigo.githubusersapp.ui.theme.ThemeMode.System


data class GithubColors(
    val primary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val iconTint: Color,
    val divider: Color,
)

object GithubLightPalettes {
    val Default =
        GithubColors(
            primary = Color(0xFF24292E),
            background = Color(0xFFF6F8FA),
            surface = Color.White,
            textPrimary = Color(0xFF24292E),
            textSecondary = Color(0xFF57606A),
            iconTint = Color(0xFF24292E),
            divider = Color(0xFFE1E4E8),
        )
}


object GithubDarkPalettes {
    val Default =
        GithubColors(
            primary = Color(0xFF58A6FF),
            background = Color(0xFF0D1117),
            surface = Color(0xFF161B22),
            textPrimary = Color(0xFFC9D1D9),
            textSecondary = Color(0xFF8B949E),
            iconTint = Color(0xFFC9D1D9),
            divider = Color(0xFF30363D),
        )
}

val LocalGithubColors =
    staticCompositionLocalOf { GithubLightPalettes.Default }

enum class ThemeMode { Light, Dark, System }


val AppFontFamily =
    FontFamily(
        Font(R.font.mulish_light, FontWeight.Light),
        Font(R.font.mulish_regular, FontWeight.Normal),
        Font(R.font.mulish_medium, FontWeight.Medium),
        Font(R.font.mulish_semibold, FontWeight.SemiBold),
        Font(R.font.mulish_bold, FontWeight.Bold),
        Font(R.font.mulish_extrabold, FontWeight.ExtraBold),
    )

val AppTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            ),
    )


@Composable
private fun updateStatusBar(
    colors: GithubColors,
    isDark: Boolean,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !isDark
        }
    }
}



@Composable
fun GithubUsersAppTheme(
    themeMode: ThemeMode = System,
    content: @Composable () -> Unit,
) {
    val isDark =
        when (themeMode) {
            Light -> false
            Dark -> true
            System -> isSystemInDarkTheme()
        }

    val colors =
        if (isDark) GithubDarkPalettes.Default
        else GithubLightPalettes.Default

    updateStatusBar(colors, isDark)

    CompositionLocalProvider(LocalGithubColors provides colors) {
        MaterialTheme(
            typography = AppTypography,
            content = content,
        )
    }
}

