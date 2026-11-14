package dev.dhruva.mycastnowrecuriter.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object MyCastNowColors {
    // Primary Brand Blue
    val PrimaryBlue = Color(0xFF0866F5)
    val PrimaryBlueTint = Color(0xFFEEF5FF)
    val PrimaryBlueLight = Color(0xFFE4EDFF)
    val PrimaryBlueDark = Color(0xFF0548B8)
    val PrimaryBlueSubtle = Color(0xFFDAE8FF)

    // Gradients
    val PrimaryGradient = Brush.linearGradient(
        listOf(Color(0xFF0B6DFA), Color(0xFF0548B8))
    )
    val HeroCardGradient = Brush.linearGradient(
        listOf(Color(0xFF0967F5), Color(0xFF033E9E))
    )
    val CardSubtleGradient = Brush.verticalGradient(
        listOf(Color(0xFFFFFFFF), Color(0xFFF9FBFF))
    )
    val AmberBadgeGradient = Brush.horizontalGradient(
        listOf(Color(0xFFFFF9EA), Color(0xFFFFF2D6))
    )
    val GreenBadgeGradient = Brush.horizontalGradient(
        listOf(Color(0xFFEAF5EF), Color(0xFFD9EFE2))
    )

    // Ink & Typography
    val InkPrimary = Color(0xFF111A36)       // Deep navy heading and text
    val InkSecondary = Color(0xFF64718A)     // Cool slate secondary text
    val InkMuted = Color(0xFF79859A)         // Placeholder text
    val InkLight = Color(0xFF94A3B8)
    val NavInactive = Color(0xFF69768B)

    // Surfaces & Backgrounds
    val BackgroundWhite = Color(0xFFFFFFFF)
    val SurfaceNeutral = Color(0xFFF7F9FC)
    val SurfaceLight = Color(0xFFF4F6F9)
    val SurfaceCard = Color(0xFFFFFFFF)
    val SurfaceBorder = Color(0xFFE5E9F0)
    val SurfaceBorderSubtle = Color(0xFFECEFF5)
    val SurfaceDivider = Color(0xFFE7ECF3)
    val SurfaceProgressTrack = Color(0xFFE8EDF4)

    // Semantic Accents
    val SuccessGreen = Color(0xFF17885D)
    val SuccessGreenTint = Color(0xFFEAF5EF)
    val SuccessGreenDark = Color(0xFF0F6744)
    val AlertAmber = Color(0xFF8A6208)
    val AlertAmberBg = Color(0xFFFFF9EA)
    val WarningStar = Color(0xFFF59E0B)
    val UrgentRed = Color(0xFFD95E42)
    val ErrorRed = Color(0xFFD95E42)
    val ErrorRedTint = Color(0xFFFFEEEE)
    val NotificationRed = Color(0xFFFF4E58)
    val HomeBar = Color(0xFF626C7E)
    val PurpleAccent = Color(0xFF6366F1)
    val PurpleTint = Color(0xFFEEF2FF)
}
