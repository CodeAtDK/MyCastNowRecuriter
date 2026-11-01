package dev.dhruva.mycastnowrecuriter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors

@Composable
fun BrandsBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MyCastNowColors.PrimaryBlueTint)
            .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(MyCastNowColors.SuccessGreen)
        )
        Text(
            text = "BRANDS",
            color = MyCastNowColors.PrimaryBlue,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp
        )
    }
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MyCastNowColors.PrimaryBlueTint,
    textColor: Color = MyCastNowColors.PrimaryBlue,
    fontSize: Int = 10
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 9.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.2.sp
        )
    }
}

@Composable
fun FilterTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral
    val fg = if (isSelected) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
    val borderCol = if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun RbiEscrowBadge(
    modifier: Modifier = Modifier,
    text: String = "RBI Nodal"
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MyCastNowColors.BackgroundWhite)
            .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(13.dp))
        Text(
            text = text,
            color = MyCastNowColors.PrimaryBlue,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VerifiedGstinBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MyCastNowColors.PrimaryBlueTint)
            .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CheckmarkGlyph(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(11.dp))
        Text(
            text = "VERIFIED GSTIN",
            color = MyCastNowColors.PrimaryBlue,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
fun UserAvatarCircle(
    letter: String = "S",
    size: Dp = 34.dp,
    backgroundColor: Color = MyCastNowColors.SurfaceDivider,
    textColor: Color = MyCastNowColors.InkPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = textColor,
            fontSize = if (letter.length > 1) (size.value * 0.36f).sp else (size.value * 0.44f).sp,
            fontWeight = FontWeight.Bold
        )
    }
}
