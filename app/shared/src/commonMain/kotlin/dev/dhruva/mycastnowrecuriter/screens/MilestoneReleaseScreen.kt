package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import dev.dhruva.mycastnowrecuriter.components.CheckmarkGlyph
import dev.dhruva.mycastnowrecuriter.components.RbiEscrowBadge
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.theme.BannerShape
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.MilestoneReleaseEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.MilestoneReleaseViewModel

@Composable
fun MilestoneReleaseScreen(
    onBackClick: () -> Unit,
    onConfirmReleaseClick: () -> Unit,
    viewModel: MilestoneReleaseViewModel = remember { AppContainer.createMilestoneReleaseViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is MilestoneReleaseEvent.NavigateToWrapUp -> onConfirmReleaseClick()
                is MilestoneReleaseEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Release milestone",
            subtitle = "Review the rights before you confirm",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hero Milestone Banner
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
                                    text = "MILESTONE APPROVED & READY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.PrimaryBlueSubtle,
                                    letterSpacing = 0.6.sp
                                )
                            }
                            RbiEscrowBadge(text = "APPROVED")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = uiState.payoutFormatted,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite,
                            letterSpacing = (-0.5).sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${uiState.creatorName} · Instagram Reel + 30-day Ads rights",
                            fontSize = 11.sp,
                            color = MyCastNowColors.PrimaryBlueLight,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Approved Deliverable Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Approved deliverable details",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "1× Instagram Reel · 4K HDR",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )
                        }
                        Text(
                            text = "Approved by Studio North · Today at 3:42 PM",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                        Text(
                            text = "Draft video asset and checksum hash logged permanently to escrow ledger.",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                    }
                }
            }

            // Rights Transfer Checklist Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Rights transferred upon release",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    RightsChecklistRow(
                        title = "Commercial usage license",
                        desc = "Worldwide digital and paid advertising rights for 30 calendar days."
                    )

                    RightsChecklistRow(
                        title = "Creator brand whitelisting",
                        desc = "Permission to run Meta ads directly through creator's authenticated handle."
                    )

                    RightsChecklistRow(
                        title = "High-resolution master footage",
                        desc = "Downloadable ProRes raw files and full color-graded deliverables."
                    )
                }
            }

            // Payout breakdown
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Milestone payout to creator", fontSize = 12.sp, color = MyCastNowColors.InkSecondary)
                            Text(text = "₹24,000.00", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MyCastNowColors.InkPrimary)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Remaining in campaign escrow", fontSize = 12.sp, color = MyCastNowColors.InkSecondary)
                            Text(text = "₹4,320.00", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MyCastNowColors.PrimaryBlue)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Bottom CTA Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .border(1.dp, MyCastNowColors.SurfaceBorder)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { viewModel.onConfirmReleaseClicked() },
                enabled = !uiState.isReleasing,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyCastNowColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (uiState.isReleasing) "Processing release..." else "Confirm & release ${uiState.payoutFormatted} →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.BackgroundWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RightsChecklistRow(
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(MyCastNowColors.SuccessGreenTint),
            contentAlignment = Alignment.Center
        ) {
            CheckmarkGlyph(tint = MyCastNowColors.SuccessGreen, modifier = Modifier.size(11.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = MyCastNowColors.InkSecondary
            )
        }
    }
}
