package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.*
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.model.Conversation
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.MessagesEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.MessagesViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun MessagesScreen(
    onOpenConversation: (Conversation) -> Unit = {},
    onOpenDeliverableReview: () -> Unit = {},
    onOpenCreatorProfile: (String) -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: MessagesViewModel = remember { AppContainer.createMessagesViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is MessagesEvent.NavigateToDeliverableReview -> onOpenDeliverableReview()
                is MessagesEvent.NavigateToCreatorProfile -> onOpenCreatorProfile(event.creatorId)
                is MessagesEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        TabAppHeader(
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header title & subtitle
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = "Messages",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real-time communication with talent, teams and production coordinators.",
                        fontSize = 12.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }
            }

            // Search bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SearchLensIcon()

                        Box(modifier = Modifier.weight(1f)) {
                            if (uiState.searchQuery.isEmpty()) {
                                Text(
                                    text = "Search conversations or briefs...",
                                    fontSize = 12.sp,
                                    color = MyCastNowColors.InkMuted
                                )
                            }
                            BasicTextField(
                                value = uiState.searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 12.sp,
                                    color = MyCastNowColors.InkPrimary,
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(MyCastNowColors.PrimaryBlue),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (uiState.searchQuery.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(MyCastNowColors.SurfaceDivider)
                                    .clickable { viewModel.onSearchQueryChanged("") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✕",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filters) { filter ->
                        FilterTabChip(
                            text = filter,
                            isSelected = filter == uiState.selectedFilter,
                            onClick = { viewModel.onFilterSelected(filter) }
                        )
                    }
                }
            }

            // Section title
            item {
                Text(
                    text = "Recent conversations",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            }

            items(uiState.conversations, key = { it.id }) { conv ->
                ConversationItemRow(
                    conversation = conv,
                    onClick = {
                        viewModel.onConversationClicked(conv)
                        onOpenConversation(conv)
                    }
                )
            }

            // Escrow Trust banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.5f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), CardShape)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Brief-linked communication",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Text(
                                text = "All scope changes, approvals and delivery timelines agreed here are legally binding under your escrow agreement.",
                                fontSize = 10.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Interactive Quick Chat Sheet
    uiState.activeConversation?.let { conv ->
        AlertDialog(
            onDismissRequest = { viewModel.onCloseConversation() },
            title = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = conv.creatorName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        CheckmarkGlyph(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(12.dp))
                    }
                    Text(
                        text = conv.campaignTitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.PrimaryBlue
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MyCastNowColors.SurfaceNeutral)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "Latest update (${conv.timestamp}):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MyCastNowColors.InkSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${conv.lastMessage}”",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MyCastNowColors.InkPrimary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MyCastNowColors.PrimaryBlueTint)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(13.dp))
                            Text(
                                text = "RBI nodal escrow active for this brief",
                                fontSize = 10.sp,
                                color = MyCastNowColors.PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (conv.creatorId == "creator_1") {
                        Button(
                            onClick = {
                                viewModel.onReviewDraftClicked(conv)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Review Draft Deliverable  →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.BackgroundWhite
                            )
                        }
                    } else if (conv.creatorId.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                viewModel.onViewCreatorProfileClicked(conv.creatorId)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MyCastNowColors.PrimaryBlue)
                        ) {
                            Text(
                                text = "View Creator Rate Card",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = { viewModel.onCloseConversation() }
                    ) {
                        Text("Close", color = MyCastNowColors.InkSecondary, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            viewModel.onCloseConversation()
                            onOpenConversation(conv)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Open Full Chat  →", color = MyCastNowColors.BackgroundWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}

@Composable
private fun ConversationItemRow(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(MyCastNowColors.BackgroundWhite)
            .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // High-resolution Avatar or Photo
            when (conversation.creatorId) {
                "creator_1" -> {
                    Image(
                        painter = painterResource(MockData.aditiCreator.imageResource!!),
                        contentDescription = conversation.creatorName,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                "creator_2" -> {
                    Image(
                        painter = painterResource(MockData.rohanCreator.imageResource!!),
                        contentDescription = conversation.creatorName,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MyCastNowColors.PrimaryBlueTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.initial.ifEmpty { conversation.creatorName.take(1) },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.creatorName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false).padding(end = 4.dp)
                    )
                    Text(
                        text = conversation.timestamp,
                        fontSize = 10.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = conversation.campaignTitle,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MyCastNowColors.PrimaryBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 11.sp,
                        color = MyCastNowColors.InkSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(end = 6.dp)
                    )

                    if (conversation.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(MyCastNowColors.PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${conversation.unreadCount}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.BackgroundWhite
                            )
                        }
                    }
                }
            }
        }
    }
}
