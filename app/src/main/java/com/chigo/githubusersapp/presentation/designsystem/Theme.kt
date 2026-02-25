package com.chigo.githubusersapp.presentation.designsystem


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.chigo.githubusersapp.R


val LocalGithubColors = staticCompositionLocalOf { LightColorScheme }

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = BackgroundWhite,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = BackgroundWhite,
    secondary = SecondaryGreen,
    onSecondary = BackgroundWhite,
    secondaryContainer = SecondaryGreen,
    onSecondaryContainer = BackgroundWhite,
    tertiary = SecondaryMediumBlue,
    onTertiary = BackgroundWhite,
    background = BackgroundWhite,
    onBackground = TextPrimary,
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLightBlue,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = BackgroundWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDeep,
    onPrimary = BackgroundWhite,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = BackgroundWhite,
    secondary = SecondaryGreen,
    onSecondary = BackgroundWhite,
    secondaryContainer = SecondaryGreen,
    onSecondaryContainer = BackgroundWhite,
    tertiary = SecondaryMediumBlue,
    onTertiary = BackgroundWhite,
    background = GrayMedium,
    onBackground = BackgroundWhite,
    surface = GrayMedium,
    onSurface = BackgroundWhite,
    surfaceVariant = GrayLight,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = BackgroundWhite
)

// Typography
val AppFontFamily = FontFamily(
    Font(R.font.mulish_light, FontWeight.Light),
    Font(R.font.mulish_regular, FontWeight.Normal),
    Font(R.font.mulish_medium, FontWeight.Medium),
    Font(R.font.mulish_semibold, FontWeight.SemiBold),
    Font(R.font.mulish_bold, FontWeight.Bold),
    Font(R.font.mulish_extrabold, FontWeight.ExtraBold),
)

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

@Composable
fun GithubUsersAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    CompositionLocalProvider(LocalGithubColors provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}


