package dev.dhruva.mycastnowrecuriter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors

import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TabAppHeader(
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            MyCastNowLogo(size = 26.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MyCastNow",
                color = MyCastNowColors.InkPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(6.dp))
            BrandsBadge()
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Notification Bell Button
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(MyCastNowColors.SurfaceNeutral)
                .border(1.dp, MyCastNowColors.SurfaceBorder, CircleShape)
                .clickable { onNotificationsClick() },
            contentAlignment = Alignment.Center
        ) {
            BellIcon(hasUnread = true)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Recruiter Avatar Circle
        UserAvatarCircle(
            letter = "SN",
            size = 34.dp,
            backgroundColor = MyCastNowColors.PrimaryBlueTint,
            textColor = MyCastNowColors.PrimaryBlue,
            modifier = Modifier
                .border(1.5.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.3f), CircleShape)
                .clickable { onProfileClick() }
        )
    }
}

@Composable
fun SubscreenHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    onOptionsClick: () -> Unit = {},
    subtitleColor: Color = MyCastNowColors.PrimaryBlue,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button with vector chevron
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MyCastNowColors.SurfaceNeutral)
                .border(1.dp, MyCastNowColors.SurfaceBorder, CircleShape)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            ChevronLeftIcon(tint = MyCastNowColors.InkPrimary)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary,
                letterSpacing = (-0.2).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = subtitleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // More options with vector dots
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MyCastNowColors.SurfaceNeutral)
                .border(1.dp, MyCastNowColors.SurfaceBorder, CircleShape)
                .clickable { onOptionsClick() },
            contentAlignment = Alignment.Center
        ) {
            MoreHorizontalIcon(tint = MyCastNowColors.PrimaryBlue)
        }
    }
}
