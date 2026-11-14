package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.*
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.DeliverableReviewEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.DeliverableReviewViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeliverableReviewScreen(
    campaignTitle: String = "Nike India · Aditi Sharma",
    onBackClick: () -> Unit,
    onApproveDeliverableClick: () -> Unit,
    onRequestRevisionClick: () -> Unit = {},
    viewModel: DeliverableReviewViewModel = remember { AppContainer.createDeliverableReviewViewModel() },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(campaignTitle) {
        viewModel.init(campaignTitle)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is DeliverableReviewEvent.NavigateToMilestoneRelease -> onApproveDeliverableClick()
                is DeliverableReviewEvent.ShowMessage -> onRequestRevisionClick()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Review deliverable",
            subtitle = uiState.campaignTitle,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status and SLA Badges
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(
                        text = "DELIVERABLE 1 OF 3",
                        backgroundColor = MyCastNowColors.PrimaryBlueTint,
                        textColor = MyCastNowColors.PrimaryBlue
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        ClockIcon(tint = MyCastNowColors.UrgentRed)
                        StatusBadge(
                            text = "Review SLA: 18h remaining",
                            backgroundColor = Color(0xFFFFF0ED),
                            textColor = MyCastNowColors.UrgentRed
                        )
                    }
                }
            }

            // Draft Video Player Card with Scrub bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(CardShape)
                        .background(MyCastNowColors.InkPrimary)
                        .clickable { viewModel.togglePlayPause() }
                ) {
                    // Video thumbnail background
                    MockData.aditiCreator.imageResource?.let { res ->
                        Image(
                            painter = painterResource(res),
                            contentDescription = "Video Draft",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Scrim gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.35f),
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )

                    // Draft preview pill & 4K badge
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MyCastNowColors.BackgroundWhite.copy(alpha = 0.9f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (uiState.isPlaying) "PLAYING DRAFT" else "DRAFT PREVIEW",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "4K HDR 60fps",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Center Glassmorphic Play/Pause Button
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.92f))
                            .align(Alignment.Center)
                            .clickable { viewModel.togglePlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isPlaying) {
                            Text(
                                text = "❚❚",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )
                        } else {
                            PlayGlyph(tint = MyCastNowColors.PrimaryBlue)
                        }
                    }

                    // Video Info & Scrubber bottom overlay
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Nykaa Summer Glow 2025 · Reel Cut",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "00:14 / 00:24 · Uploaded today at 10:42 AM",
                            fontSize = 10.sp,
                            color = MyCastNowColors.PrimaryBlueLight
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Video Scrub Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.58f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MyCastNowColors.PrimaryBlue)
                            )
                        }
                    }
                }
            }

            // Creator note card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(14.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "💬", fontSize = 16.sp)
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "Creator note from Aditi Sharma",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Text(
                                text = "“Updated the opening 3-second hook with the new serum dispenser close-up and natural lighting. Ready for your feedback!”",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Review Checklist Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Review checklist",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "${uiState.checkedCount} of ${uiState.checklist.size} confirmed",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (uiState.allChecked) MyCastNowColors.SuccessGreen else MyCastNowColors.PrimaryBlue
                        )
                    }

                    uiState.checklist.forEachIndexed { idx, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.toggleChecklistItem(idx)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (item.isChecked) MyCastNowColors.SuccessGreenTint else MyCastNowColors.SurfaceNeutral)
                                    .border(
                                        1.dp,
                                        if (item.isChecked) MyCastNowColors.SuccessGreen else MyCastNowColors.SurfaceBorder,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.isChecked) {
                                    CheckmarkGlyph(tint = MyCastNowColors.SuccessGreen)
                                }
                            }

                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MyCastNowColors.InkPrimary
                            )
                        }
                    }
                }
            }

            // Escrow protection guarantee card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.AlertAmberBg)
                        .border(1.dp, MyCastNowColors.AlertAmber.copy(alpha = 0.25f), CardShape)
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Milestone ₹24,000 releases only upon your approval",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.AlertAmber
                        )
                        Text(
                            text = "If adjustments are required, you have 1 included revision round before payout.",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Bottom Action Bar (Dual Buttons)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .border(1.dp, MyCastNowColors.SurfaceBorder)
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.openRevisionModal() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MyCastNowColors.SurfaceBorder),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 11.dp)
                ) {
                    Text(
                        text = "Request revision",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = { viewModel.approveDeliverable() },
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyCastNowColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 11.dp)
                ) {
                    Text(
                        text = if (uiState.isSubmitting) "Approving..." else "Approve deliverable →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.BackgroundWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = "Revision round 1 of 1 available · Escrow secured",
                fontSize = 10.sp,
                color = MyCastNowColors.InkSecondary
            )
        }
    }

    // Interactive Revision Request Dialog
    if (uiState.showRevisionModal) {
        AlertDialog(
            onDismissRequest = { viewModel.closeRevisionModal() },
            title = {
                Text(
                    text = "Request a Revision",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Specify feedback for creator:",
                        fontSize = 12.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                    OutlinedTextField(
                        value = uiState.revisionNotes,
                        onValueChange = { viewModel.updateRevisionNotes(it) },
                        placeholder = { Text("e.g., Please brighten the product shot at 0:12...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitRevisionRequest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue)
                ) {
                    Text("Send to Creator", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeRevisionModal() }) {
                    Text("Cancel", color = MyCastNowColors.InkSecondary)
                }
            }
        )
    }
}
