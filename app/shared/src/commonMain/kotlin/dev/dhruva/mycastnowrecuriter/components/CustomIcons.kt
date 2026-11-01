package dev.dhruva.mycastnowrecuriter.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MyCastNowLogo(
    modifier: Modifier = Modifier,
    size: Dp = 26.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val scaleX = w / 44f
        val scaleY = h / 29f

        val path1 = Path().apply {
            moveTo(0f * scaleX, 0f * scaleY)
            lineTo(6f * scaleX, 0f * scaleY)
            lineTo(22f * scaleX, 11f * scaleY)
            lineTo(38f * scaleX, 0f * scaleY)
            lineTo(44f * scaleX, 0f * scaleY)
            lineTo(44f * scaleX, 29f * scaleY)
            lineTo(38f * scaleX, 29f * scaleY)
            lineTo(38f * scaleX, 11f * scaleY)
            lineTo(22f * scaleX, 22f * scaleY)
            lineTo(6f * scaleX, 11f * scaleY)
            lineTo(6f * scaleX, 29f * scaleY)
            lineTo(0f * scaleX, 29f * scaleY)
            close()
        }
        drawPath(path1, color = MyCastNowColors.PrimaryBlue)

        val path2 = Path().apply {
            moveTo(22f * scaleX, 11f * scaleY)
            lineTo(38f * scaleX, 0f * scaleY)
            lineTo(38f * scaleX, 11f * scaleY)
            lineTo(22f * scaleX, 22f * scaleY)
            close()
        }
        drawPath(path2, color = MyCastNowColors.InkPrimary)
    }
}

@Composable
fun BellIcon(
    modifier: Modifier = Modifier,
    hasUnread: Boolean = true,
    tint: Color = MyCastNowColors.InkPrimary
) {
    Box(modifier = modifier.size(34.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(19.dp)) {
            val w = size.width
            val h = size.height

            val bellPath = Path().apply {
                moveTo(w * 0.5f, h * 0.12f)
                cubicTo(w * 0.28f, h * 0.12f, w * 0.18f, h * 0.35f, w * 0.18f, h * 0.62f)
                lineTo(w * 0.1f, h * 0.78f)
                lineTo(w * 0.9f, h * 0.78f)
                lineTo(w * 0.82f, h * 0.62f)
                cubicTo(w * 0.82f, h * 0.35f, w * 0.72f, h * 0.12f, w * 0.5f, h * 0.12f)
                close()
            }
            drawPath(bellPath, color = tint, style = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))

            // Clapper bottom arc
            drawArc(
                color = tint,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(w * 0.37f, h * 0.78f),
                size = Size(w * 0.26f, h * 0.18f)
            )
        }

        if (hasUnread) {
            Canvas(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-3).dp, y = 3.dp)
            ) {
                drawCircle(color = Color.White, radius = size.width / 2f)
                drawCircle(color = MyCastNowColors.NotificationRed, radius = size.width * 0.38f)
            }
        }
    }
}

@Composable
fun SearchLensIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.InkMuted
) {
    Canvas(modifier = modifier.size(18.dp)) {
        val r = size.width * 0.34f
        val center = Offset(size.width * 0.42f, size.height * 0.42f)
        drawCircle(color = tint, radius = r, center = center, style = Stroke(width = 2f))
        drawLine(
            color = tint,
            start = Offset(size.width * 0.68f, size.height * 0.68f),
            end = Offset(size.width * 0.95f, size.height * 0.95f),
            strokeWidth = 2.4f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun FilterSlidersIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.InkPrimary
) {
    Canvas(modifier = modifier.size(18.dp)) {
        val w = size.width
        val h = size.height

        // Line 1
        drawLine(tint, Offset(w * 0.05f, h * 0.25f), Offset(w * 0.95f, h * 0.25f), strokeWidth = 2f, cap = StrokeCap.Round)
        drawCircle(tint, radius = 3f, center = Offset(w * 0.35f, h * 0.25f))

        // Line 2
        drawLine(tint, Offset(w * 0.05f, h * 0.5f), Offset(w * 0.95f, h * 0.5f), strokeWidth = 2f, cap = StrokeCap.Round)
        drawCircle(tint, radius = 3f, center = Offset(w * 0.72f, h * 0.5f))

        // Line 3
        drawLine(tint, Offset(w * 0.05f, h * 0.75f), Offset(w * 0.95f, h * 0.75f), strokeWidth = 2f, cap = StrokeCap.Round)
        drawCircle(tint, radius = 3f, center = Offset(w * 0.42f, h * 0.75f))
    }
}

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit = {},
    isEditable: Boolean = false,
    modifier: Modifier = Modifier,
    starSize: Dp = 16.dp
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..5) {
            val color = if (i <= rating) MyCastNowColors.WarningStar else MyCastNowColors.SurfaceBorder
            Canvas(
                modifier = Modifier
                    .size(starSize)
                    .clickable(enabled = isEditable) { onRatingChanged(i) }
            ) {
                val path = Path().apply {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val rOuter = size.width / 2f
                    val rInner = rOuter * 0.44f
                    for (step in 0 until 10) {
                        val angle = (step * 36 - 90) * (PI / 180.0)
                        val r = if (step % 2 == 0) rOuter else rInner
                        val x = (cx + r * cos(angle)).toFloat()
                        val y = (cy + r * sin(angle)).toFloat()
                        if (step == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path, color = color)
            }
        }
    }
}

@Composable
fun CheckmarkGlyph(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.SuccessGreen,
    strokeWidth: Float = 2.4f
) {
    Canvas(modifier = modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.52f)
            lineTo(w * 0.42f, h * 0.80f)
            lineTo(w * 0.88f, h * 0.22f)
        }
        drawPath(path, color = tint, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun PlayGlyph(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.PrimaryBlue
) {
    Canvas(modifier = modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.28f, h * 0.16f)
            lineTo(w * 0.84f, h * 0.5f)
            lineTo(w * 0.28f, h * 0.84f)
            close()
        }
        drawPath(path, color = tint)
    }
}

@Composable
fun ChevronLeftIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.InkPrimary,
    strokeWidth: Float = 2.4f
) {
    Canvas(modifier = modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.68f, h * 0.15f)
            lineTo(w * 0.28f, h * 0.5f)
            lineTo(w * 0.68f, h * 0.85f)
        }
        drawPath(path, color = tint, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun MoreHorizontalIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.PrimaryBlue
) {
    Canvas(modifier = modifier.size(18.dp)) {
        val cy = size.height / 2f
        val r = 2.4f
        drawCircle(color = tint, radius = r, center = Offset(size.width * 0.2f, cy))
        drawCircle(color = tint, radius = r, center = Offset(size.width * 0.5f, cy))
        drawCircle(color = tint, radius = r, center = Offset(size.width * 0.8f, cy))
    }
}

@Composable
fun BookmarkIcon(
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    activeTint: Color = MyCastNowColors.PrimaryBlue,
    inactiveTint: Color = MyCastNowColors.InkSecondary
) {
    Canvas(modifier = modifier.size(18.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.2f, h * 0.1f)
            lineTo(w * 0.8f, h * 0.1f)
            lineTo(w * 0.8f, h * 0.9f)
            lineTo(w * 0.5f, h * 0.68f)
            lineTo(w * 0.2f, h * 0.9f)
            close()
        }
        if (isBookmarked) {
            drawPath(path, color = activeTint)
        } else {
            drawPath(path, color = inactiveTint, style = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

@Composable
fun ShieldLockIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.PrimaryBlue
) {
    Canvas(modifier = modifier.size(16.dp)) {
        val w = size.width
        val h = size.height
        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.08f)
            lineTo(w * 0.9f, h * 0.22f)
            cubicTo(w * 0.9f, h * 0.65f, w * 0.65f, h * 0.88f, w * 0.5f, h * 0.95f)
            cubicTo(w * 0.35f, h * 0.88f, w * 0.1f, h * 0.65f, w * 0.1f, h * 0.22f)
            close()
        }
        drawPath(shieldPath, color = tint, style = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Center check or dot
        val checkPath = Path().apply {
            moveTo(w * 0.35f, h * 0.50f)
            lineTo(w * 0.47f, h * 0.62f)
            lineTo(w * 0.68f, h * 0.38f)
        }
        drawPath(checkPath, color = tint, style = Stroke(width = 1.6f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun SparkleIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.WarningStar
) {
    Canvas(modifier = modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val path = Path().apply {
            moveTo(cx, 0f)
            cubicTo(cx, cy * 0.6f, cx * 1.4f, cy, w, cy)
            cubicTo(cx * 1.4f, cy, cx, cy * 1.4f, cx, h)
            cubicTo(cx, cy * 1.4f, cx * 0.6f, cy, 0f, cy)
            cubicTo(cx * 0.6f, cy, cx, cy * 0.6f, cx, 0f)
            close()
        }
        drawPath(path, color = tint)
    }
}

@Composable
fun ClockIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.InkSecondary
) {
    Canvas(modifier = modifier.size(14.dp)) {
        val r = size.width / 2f - 1f
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = tint, radius = r, center = center, style = Stroke(width = 1.6f))
        drawLine(tint, center, Offset(center.x, center.y - r * 0.55f), strokeWidth = 1.6f, cap = StrokeCap.Round)
        drawLine(tint, center, Offset(center.x + r * 0.45f, center.y), strokeWidth = 1.6f, cap = StrokeCap.Round)
    }
}

@Composable
fun TrendUpIcon(
    modifier: Modifier = Modifier,
    tint: Color = MyCastNowColors.SuccessGreen
) {
    Canvas(modifier = modifier.size(14.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.1f, h * 0.85f)
            lineTo(w * 0.45f, h * 0.45f)
            lineTo(w * 0.65f, h * 0.62f)
            lineTo(w * 0.9f, h * 0.2f)
        }
        drawPath(path, color = tint, style = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        // Arrow head
        val arrow = Path().apply {
            moveTo(w * 0.65f, h * 0.2f)
            lineTo(w * 0.9f, h * 0.2f)
            lineTo(w * 0.9f, h * 0.45f)
        }
        drawPath(arrow, color = tint, style = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
