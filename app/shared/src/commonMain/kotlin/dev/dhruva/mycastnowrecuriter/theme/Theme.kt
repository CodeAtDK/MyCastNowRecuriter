package dev.dhruva.mycastnowrecuriter.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = MyCastNowColors.PrimaryBlue,
    onPrimary = MyCastNowColors.BackgroundWhite,
    primaryContainer = MyCastNowColors.PrimaryBlueTint,
    onPrimaryContainer = MyCastNowColors.PrimaryBlue,
    secondary = MyCastNowColors.InkSecondary,
    onSecondary = MyCastNowColors.BackgroundWhite,
    background = MyCastNowColors.BackgroundWhite,
    onBackground = MyCastNowColors.InkPrimary,
    surface = MyCastNowColors.BackgroundWhite,
    onSurface = MyCastNowColors.InkPrimary,
    surfaceVariant = MyCastNowColors.SurfaceNeutral,
    onSurfaceVariant = MyCastNowColors.InkSecondary,
    outline = MyCastNowColors.SurfaceBorder
)

@Composable
fun MyCastNowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MyCastNowTypography,
        shapes = MyCastNowShapes,
        content = content
    )
}
