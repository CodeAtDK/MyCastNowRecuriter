package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.model.AppNotification
import dev.dhruva.mycastnowrecuriter.model.NotificationCategory
import dev.dhruva.mycastnowrecuriter.model.NotificationType
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.NotificationsEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.NotificationsViewModel

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNavigateToRoute: (String) -> Unit = {},
    viewModel: NotificationsViewModel = remember { AppContainer.createNotificationsViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is NotificationsEvent.NavigateTo -> onNavigateToRoute(event.route)
                is NotificationsEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Notifications",
            subtitle = if (uiState.unreadCount > 0) "${uiState.unreadCount} unread action items" else "All caught up",
            onBackClick = onBackClick
        )

        // Filter categories row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf(
                NotificationCategory.ALL to "All updates",
                NotificationCategory.DELIVERABLES to "🎬 Deliverables & SLAs",
                NotificationCategory.ESCROW to "🔒 Escrow & Funds",
                NotificationCategory.AUDITIONS to "👥 Auditions"
            )

            items(categories) { (cat, label) ->
                val isSelected = uiState.selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(20.dp))
                        .clickable { viewModel.selectCategory(cat) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (uiState.notifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No notifications in this category",
                            color = MyCastNowColors.InkSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(uiState.notifications) { notif ->
                    NotificationCard(
                        notification = notif,
                        onActionClick = { viewModel.onNotificationActionClicked(notif) },
                        onCardClick = { viewModel.markAsRead(notif.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: AppNotification,
    onActionClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val isUnread = !notification.isRead

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(if (isUnread) MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.35f) else MyCastNowColors.BackgroundWhite)
            .border(
                1.dp,
                if (isUnread) MyCastNowColors.PrimaryBlue.copy(alpha = 0.35f) else MyCastNowColors.SurfaceBorder,
                CardShape
            )
            .clickable { onCardClick() }
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category icon pill
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                when (notification.type) {
                                    NotificationType.ESCROW_LOCKED, NotificationType.MILESTONE_RELEASED -> MyCastNowColors.PrimaryBlueTint
                                    NotificationType.SLA_DEADLINE -> Color(0xFFFFEEEE)
                                    NotificationType.DELIVERABLE_SUBMITTED -> MyCastNowColors.SuccessGreenTint
                                    NotificationType.AUDITION_RECEIVED -> MyCastNowColors.SurfaceNeutral
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when (notification.type) {
                            NotificationType.ESCROW_LOCKED, NotificationType.MILESTONE_RELEASED ->
                                ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(16.dp))
                            NotificationType.SLA_DEADLINE -> Text("⏱️", fontSize = 14.sp)
                            NotificationType.DELIVERABLE_SUBMITTED -> Text("🎬", fontSize = 14.sp)
                            NotificationType.AUDITION_RECEIVED -> Text("👥", fontSize = 14.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = notification.title,
                                fontSize = 13.sp,
                                fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                                color = MyCastNowColors.InkPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            if (isUnread) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(MyCastNowColors.PrimaryBlue)
                                )
                            }
                        }
                        Text(
                            text = notification.timestamp,
                            fontSize = 10.sp,
                            color = MyCastNowColors.InkLight
                        )
                    }
                }
            }

            Text(
                text = notification.description,
                fontSize = 11.sp,
                color = MyCastNowColors.InkSecondary,
                lineHeight = 16.sp
            )

            notification.actionLabel?.let { label ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onActionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$label  →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite
                        )
                    }
                }
            }
        }
    }
}
