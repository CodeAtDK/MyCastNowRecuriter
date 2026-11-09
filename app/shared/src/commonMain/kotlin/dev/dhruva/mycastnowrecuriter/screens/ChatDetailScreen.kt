package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.ChevronLeftIcon
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.model.ChatMessage
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.ChatDetailEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.ChatDetailViewModel

@Composable
fun ChatDetailScreen(
    conversationId: String = "chat_1",
    creatorName: String = "Aditi Sharma",
    campaignTitle: String = "Nike India Summer Reel",
    onBackClick: () -> Unit,
    onOpenDeliverableReview: (String) -> Unit = {},
    onOpenCreatorProfile: () -> Unit = {},
    viewModel: ChatDetailViewModel = remember { AppContainer.createChatDetailViewModel() },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(conversationId) {
        viewModel.init(conversationId, creatorName, campaignTitle)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ChatDetailEvent.NavigateToDeliverableReview -> onOpenDeliverableReview(event.campaignTitle)
                is ChatDetailEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        // Custom Chat Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = uiState.creatorName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(MyCastNowColors.SuccessGreen)
                    )
                }
                Text(
                    text = uiState.campaignTitle,
                    fontSize = 11.sp,
                    color = MyCastNowColors.InkSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TextButton(
                onClick = onOpenCreatorProfile,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Profile",
                    color = MyCastNowColors.PrimaryBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MyCastNowColors.SurfaceDivider)
        )

        // Pinned Escrow Scope Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.4f))
                .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.15f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                ) {
                    ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(14.dp))
                    Text(
                        text = "₹28,320 in Escrow  ·  1× 4K Reel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.InkPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Review Draft →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.PrimaryBlue,
                    maxLines = 1,
                    modifier = Modifier.clickable { viewModel.onDeliverableAttachmentClick() }
                )
            }
        }

        // Messages Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(uiState.messages) { msg ->
                ChatMessageBubble(
                    message = msg,
                    onAttachmentClick = { viewModel.onDeliverableAttachmentClick() }
                )
            }
        }

        // Input bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .border(1.dp, MyCastNowColors.SurfaceBorder)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MyCastNowColors.SurfaceNeutral),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📎", fontSize = 16.sp)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    if (uiState.messageInput.isEmpty()) {
                        Text(
                            text = "Message ${uiState.creatorName}...",
                            fontSize = 12.sp,
                            color = MyCastNowColors.InkLight
                        )
                    }
                    BasicTextField(
                        value = uiState.messageInput,
                        onValueChange = { viewModel.onInputChanged(it) },
                        textStyle = TextStyle(
                            color = MyCastNowColors.InkPrimary,
                            fontSize = 12.sp
                        ),
                        cursorBrush = SolidColor(MyCastNowColors.PrimaryBlue),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MyCastNowColors.PrimaryBlue)
                        .clickable { viewModel.onSendMessage() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("➤", color = MyCastNowColors.BackgroundWhite, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    onAttachmentClick: () -> Unit
) {
    val isMe = message.isFromMe

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isMe) 14.dp else 2.dp,
                        bottomEnd = if (isMe) 2.dp else 14.dp
                    )
                )
                .background(if (isMe) MyCastNowColors.PrimaryBlue else MyCastNowColors.BackgroundWhite)
                .border(
                    1.dp,
                    if (isMe) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder,
                    RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = message.text,
                    fontSize = 12.sp,
                    color = if (isMe) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary,
                    lineHeight = 16.sp
                )

                if (message.hasAttachment) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isMe) MyCastNowColors.PrimaryBlueDark else MyCastNowColors.PrimaryBlueTint)
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🎬 ${message.attachmentTitle ?: "Deliverable Attachment"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMe) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                            )
                            message.attachmentSubtitle?.let { sub ->
                                Text(
                                    text = sub,
                                    fontSize = 10.sp,
                                    color = if (isMe) MyCastNowColors.BackgroundWhite.copy(alpha = 0.8f) else MyCastNowColors.InkSecondary
                                )
                            }
                            message.attachmentActionLabel?.let { action ->
                                Text(
                                    text = "$action  →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMe) MyCastNowColors.BackgroundWhite else MyCastNowColors.PrimaryBlue,
                                    modifier = Modifier.clickable { onAttachmentClick() }
                                )
                            }
                        }
                    }
                }

                Text(
                    text = message.timestamp,
                    fontSize = 9.sp,
                    color = if (isMe) MyCastNowColors.BackgroundWhite.copy(alpha = 0.7f) else MyCastNowColors.InkLight,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
