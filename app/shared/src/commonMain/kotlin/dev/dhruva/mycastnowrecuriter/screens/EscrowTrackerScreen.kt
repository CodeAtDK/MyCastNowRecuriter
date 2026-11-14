package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.CheckmarkGlyph
import dev.dhruva.mycastnowrecuriter.components.RbiEscrowBadge
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import dev.dhruva.mycastnowrecuriter.model.StepStatus
import dev.dhruva.mycastnowrecuriter.theme.BannerShape
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors

import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.EscrowTrackerEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.EscrowTrackerViewModel

@Composable
fun EscrowTrackerScreen(
    campaignTitle: String = "Nike India · Summer Creator Campaign",
    onBackClick: () -> Unit,
    onOpenCampaignWorkspace: () -> Unit,
    onContactSupportClick: () -> Unit = {},
    viewModel: EscrowTrackerViewModel = remember { AppContainer.createEscrowTrackerViewModel() },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(campaignTitle) {
        viewModel.init(campaignTitle)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is EscrowTrackerEvent.NavigateToDeliverableReview -> onOpenCampaignWorkspace()
                is EscrowTrackerEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Booking tracker",
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
            // Hero Escrow Locked Card
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
                                    text = "ESCROW SECURED & LOCKED",
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
                            text = uiState.escrowAmountFormatted,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Held safely in nodal escrow · Auto-releases only upon your approval",
                            fontSize = 11.sp,
                            color = MyCastNowColors.PrimaryBlueLight
                        )
                    }
                }
            }

            // Booking overview card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Booking overview",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)) {
                                Text(
                                    text = "CREATOR",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkSecondary
                                )
                                Text(
                                    text = uiState.creatorName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f, fill = false)) {
                                Text(
                                    text = "DELIVERABLES",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkSecondary
                                )
                                Text(
                                    text = uiState.deliverablesText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Ready for review actionable prompt
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint)
                        .border(1.5.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.4f), CardShape)
                        .clickable { viewModel.onOpenReviewClicked() }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MyCastNowColors.PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎬", fontSize = 18.sp)
                        }

                        Column(modifier = Modifier.weight(1f).padding(end = 6.dp)) {
                            Text(
                                text = "Draft deliverable is ready for review!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Watch Aditi's video & approve milestone or request revisions.",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }

                        Text(
                            text = "Review ›",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue,
                            maxLines = 1
                        )
                    }
                }
            }

            // Timeline header
            item {
                Text(
                    text = "Production escrow pipeline",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            }

            // Connected Timeline Items
            val steps = uiState.steps
            itemsIndexed(steps) { idx, step ->
                ConnectedTimelineStep(
                    step = step,
                    isLast = idx == steps.lastIndex
                )
            }

            // Need help banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.SurfaceNeutral)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Need help with this booking?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "Our RBI-nodal escrow desk can resolve payment, delivery or revision delays.",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Contact 24/7 priority support  →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue,
                            modifier = Modifier.clickable {
                                viewModel.onRequestSupportClicked()
                                onContactSupportClick()
                            }
                        )
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
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = { viewModel.onOpenReviewClicked() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyCastNowColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = "Open deliverable review (Draft ready)  →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.BackgroundWhite
                )
            }
        }
    }
}

@Composable
private fun ConnectedTimelineStep(
    step: EscrowTimelineStep,
    isLast: Boolean
) {
    val isCompleted = step.status == StepStatus.COMPLETED
    val isInProgress = step.status == StepStatus.IN_PROGRESS

    val (circleBg, textColor) = when {
        isCompleted -> MyCastNowColors.SuccessGreen to Color.White
        isInProgress -> MyCastNowColors.PrimaryBlue to Color.White
        else -> MyCastNowColors.SurfaceDivider to MyCastNowColors.InkSecondary
    }

    val statusLabel = when (step.status) {
        StepStatus.COMPLETED -> "Completed"
        StepStatus.IN_PROGRESS -> "In progress"
        StepStatus.UP_NEXT -> "Up next"
    }

    val statusColor = when (step.status) {
        StepStatus.COMPLETED -> MyCastNowColors.SuccessGreen
        StepStatus.IN_PROGRESS -> MyCastNowColors.PrimaryBlue
        StepStatus.UP_NEXT -> MyCastNowColors.InkSecondary
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Timeline node column with connector line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(circleBg),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    CheckmarkGlyph(tint = Color.White, modifier = Modifier.size(12.dp))
                } else {
                    Text(
                        text = "${step.stepNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(52.dp)
                ) {
                    val lineColor = if (isCompleted) MyCastNowColors.SuccessGreen else MyCastNowColors.SurfaceBorder
                    drawLine(
                        color = lineColor,
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Timeline Step Content Card
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(CardShape)
                .background(if (isInProgress) MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.35f) else MyCastNowColors.BackgroundWhite)
                .border(
                    1.dp,
                    if (isInProgress) MyCastNowColors.PrimaryBlue.copy(alpha = 0.4f) else MyCastNowColors.SurfaceBorder,
                    CardShape
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = step.description,
                        fontSize = 11.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (step.status) {
                                StepStatus.COMPLETED -> MyCastNowColors.SuccessGreenTint
                                StepStatus.IN_PROGRESS -> MyCastNowColors.PrimaryBlueTint
                                StepStatus.UP_NEXT -> MyCastNowColors.SurfaceNeutral
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
        }
    }
}
