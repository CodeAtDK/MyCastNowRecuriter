package dev.dhruva.mycastnowrecuriter.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dhruva.mycastnowrecuriter.navigation.NavigationTab
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors

@Composable
fun BottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        // Subtle top divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MyCastNowColors.SurfaceBorderSubtle)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationTab.entries.forEach { tab ->
                val isSelected = tab == currentTab
                val tint = if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.NavInactive
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onTabSelected(tab) }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            NavIconForTab(tab = tab, isSelected = isSelected, tint = tint)

                            // Unread indicator for messages
                            if (tab == NavigationTab.MESSAGES) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 4.dp, y = (-2).dp)
                                        .clip(CircleShape)
                                        .background(MyCastNowColors.NotificationRed)
                                )
                            }
                        }

                        Text(
                            text = tab.title,
                            color = tint,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
    }
}

@Composable
private fun NavIconForTab(
    tab: NavigationTab,
    isSelected: Boolean,
    tint: Color
) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        when (tab) {
            NavigationTab.DISCOVER -> {
                // Modern Explorer Compass
                val r = w * 0.42f
                val center = Offset(w / 2f, h / 2f)
                drawCircle(
                    color = tint,
                    radius = r,
                    center = center,
                    style = Stroke(width = if (isSelected) 2.2f else 1.8f)
                )

                // Diamond needle
                val needlePath = Path().apply {
                    moveTo(center.x, center.y - r * 0.72f)
                    lineTo(center.x + r * 0.35f, center.y)
                    lineTo(center.x, center.y + r * 0.72f)
                    lineTo(center.x - r * 0.35f, center.y)
                    close()
                }
                if (isSelected) {
                    drawPath(needlePath, color = tint)
                } else {
                    drawPath(needlePath, color = tint, style = Stroke(width = 1.4f))
                }
            }

            NavigationTab.CAMPAIGNS -> {
                // Executive Briefcase / Folder
                val boxLeft = w * 0.15f
                val boxTop = h * 0.32f
                val boxWidth = w * 0.7f
                val boxHeight = h * 0.54f
                val corner = 3.dp.toPx()

                // Briefcase Body
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(boxLeft, boxTop),
                    size = Size(boxWidth, boxHeight),
                    cornerRadius = CornerRadius(corner, corner),
                    style = Stroke(width = if (isSelected) 2.2f else 1.8f)
                )

                // Briefcase Handle
                val handlePath = Path().apply {
                    moveTo(w * 0.35f, boxTop)
                    lineTo(w * 0.35f, h * 0.18f)
                    lineTo(w * 0.65f, h * 0.18f)
                    lineTo(w * 0.65f, boxTop)
                }
                drawPath(handlePath, color = tint, style = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))

                // Center divider / clasp
                drawLine(tint, Offset(boxLeft, boxTop + boxHeight * 0.45f), Offset(boxLeft + boxWidth, boxTop + boxHeight * 0.45f), strokeWidth = 1.6f)
                drawCircle(tint, radius = 2.2f, center = Offset(w * 0.5f, boxTop + boxHeight * 0.45f))
            }

            NavigationTab.MESSAGES -> {
                // Chat Message Bubble
                val bubblePath = Path().apply {
                    moveTo(w * 0.15f, h * 0.22f)
                    lineTo(w * 0.85f, h * 0.22f)
                    cubicTo(w * 0.92f, h * 0.22f, w * 0.92f, h * 0.68f, w * 0.85f, h * 0.68f)
                    lineTo(w * 0.42f, h * 0.68f)
                    lineTo(w * 0.22f, h * 0.86f)
                    lineTo(w * 0.25f, h * 0.68f)
                    lineTo(w * 0.15f, h * 0.68f)
                    cubicTo(w * 0.08f, h * 0.68f, w * 0.08f, h * 0.22f, w * 0.15f, h * 0.22f)
                    close()
                }
                if (isSelected) {
                    drawPath(bubblePath, color = tint.copy(alpha = 0.15f))
                }
                drawPath(bubblePath, color = tint, style = Stroke(width = if (isSelected) 2.2f else 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round))

                // Chat horizontal lines
                drawLine(tint, Offset(w * 0.3f, h * 0.4f), Offset(w * 0.7f, h * 0.4f), strokeWidth = 1.6f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.3f, h * 0.52f), Offset(w * 0.58f, h * 0.52f), strokeWidth = 1.6f, cap = StrokeCap.Round)
            }

            NavigationTab.COMPANY -> {
                // Corporate Workspace / Building
                val bLeft = w * 0.2f
                val bTop = h * 0.18f
                val bWidth = w * 0.6f
                val bHeight = h * 0.72f

                drawRoundRect(
                    color = tint,
                    topLeft = Offset(bLeft, bTop),
                    size = Size(bWidth, bHeight),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    style = Stroke(width = if (isSelected) 2.2f else 1.8f)
                )

                // Windows
                drawRect(tint, topLeft = Offset(w * 0.32f, h * 0.30f), size = Size(w * 0.12f, h * 0.10f))
                drawRect(tint, topLeft = Offset(w * 0.56f, h * 0.30f), size = Size(w * 0.12f, h * 0.10f))
                drawRect(tint, topLeft = Offset(w * 0.32f, h * 0.48f), size = Size(w * 0.12f, h * 0.10f))
                drawRect(tint, topLeft = Offset(w * 0.56f, h * 0.48f), size = Size(w * 0.12f, h * 0.10f))

                // Door
                drawRect(tint, topLeft = Offset(w * 0.42f, h * 0.68f), size = Size(w * 0.16f, h * 0.22f))
            }
        }
    }
}
