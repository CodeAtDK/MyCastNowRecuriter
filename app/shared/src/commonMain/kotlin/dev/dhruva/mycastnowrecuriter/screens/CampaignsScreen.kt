package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import dev.dhruva.mycastnowrecuriter.components.*
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.model.Campaign
import dev.dhruva.mycastnowrecuriter.model.CampaignStatus
import dev.dhruva.mycastnowrecuriter.theme.BannerShape
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors

import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.CampaignsEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.CampaignsViewModel

@Composable
fun CampaignsScreen(
    onOpenEscrowTracker: (Campaign) -> Unit,
    onOpenDeliverableReview: (Campaign) -> Unit,
    onOpenAuditionsReview: (Campaign) -> Unit = {},
    onNewBriefClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: CampaignsViewModel = remember { AppContainer.createCampaignsViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CampaignsEvent.NavigateToEscrowTracker -> onOpenEscrowTracker(event.campaign)
                is CampaignsEvent.NavigateToReview -> onOpenDeliverableReview(event.campaign)
                is CampaignsEvent.ShowMessage -> Unit
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header titles
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = "Campaigns & briefs",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real-time production tracking with escrow milestone security.",
                        fontSize = 12.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }
            }

            // Hero Escrow Banner with Gradient
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(BannerShape)
                        .background(MyCastNowColors.HeroCardGradient)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                ShieldLockIcon(tint = MyCastNowColors.PrimaryBlueSubtle, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "ESCROW SECURED & PROTECTED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.PrimaryBlueSubtle,
                                    letterSpacing = 0.6.sp
                                )
                            }
                            RbiEscrowBadge(text = "RBI Nodal")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = uiState.totalEscrowProtected,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Across ${uiState.campaigns.count { it.status == CampaignStatus.ACTIVE_SHOOT }} active productions · Funds release only after your review",
                            fontSize = 11.sp,
                            color = MyCastNowColors.PrimaryBlueLight
                        )
                    }
                }
            }

            // Filter Tabs
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

            // Section Header with "+ New brief" button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your campaigns",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    Button(
                        onClick = {
                            viewModel.onNewBriefClicked()
                            onNewBriefClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MyCastNowColors.PrimaryBlue
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = "＋ New brief",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite
                        )
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(MyCastNowColors.ErrorRedTint)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Failed to load campaigns",
                            fontSize = 12.sp,
                            color = MyCastNowColors.ErrorRed
                        )
                    }
                }
            }

            items(uiState.campaigns, key = { it.id }) { campaign ->
                CampaignCard(
                    campaign = campaign,
                    onActionClick = {
                        viewModel.onCampaignActionClicked(campaign)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CampaignCard(
    campaign: Campaign,
    onActionClick: () -> Unit
) {
    val (statusBg, statusFg) = when (campaign.status) {
        CampaignStatus.ACTIVE_SHOOT -> MyCastNowColors.SuccessGreenTint to MyCastNowColors.SuccessGreen
        CampaignStatus.MILESTONE_PENDING -> MyCastNowColors.AlertAmberBg to MyCastNowColors.AlertAmber
        CampaignStatus.CASTING_OPEN -> MyCastNowColors.PrimaryBlueTint to MyCastNowColors.PrimaryBlue
        else -> MyCastNowColors.SurfaceNeutral to MyCastNowColors.InkPrimary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(MyCastNowColors.BackgroundWhite)
            .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
            .clickable { onActionClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Status and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(
                    text = campaign.statusLabel,
                    backgroundColor = statusBg,
                    textColor = statusFg,
                    fontSize = 10
                )

                Text(
                    text = campaign.category,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkSecondary,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Title
            Text(
                text = campaign.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Talent & Budget info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 ${campaign.talentCountText}",
                    fontSize = 11.sp,
                    color = MyCastNowColors.InkSecondary
                )
                Text(
                    text = campaign.budgetFormatted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.PrimaryBlue
                )
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MyCastNowColors.SurfaceProgressTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(campaign.progressPercent / 100f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(MyCastNowColors.PrimaryBlue)
                )
            }

            // Bottom action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${campaign.progressPercent}% complete · On schedule",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MyCastNowColors.InkSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false).padding(end = 6.dp)
                )

                Button(
                    onClick = onActionClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyCastNowColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = campaign.actionLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.BackgroundWhite,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
