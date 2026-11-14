package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import dev.dhruva.mycastnowrecuriter.components.StarRatingBar
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.CampaignWrapEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.CampaignWrapViewModel

@Composable
fun CampaignWrapScreen(
    onBackClick: () -> Unit,
    onBookAgainClick: () -> Unit,
    onDownloadInvoiceClick: () -> Unit = {},
    viewModel: CampaignWrapViewModel = remember { AppContainer.createCampaignWrapViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CampaignWrapEvent.NavigateToDiscover -> onBookAgainClick()
                is CampaignWrapEvent.NavigateToCampaigns -> onBackClick()
                is CampaignWrapEvent.ShowMessage -> onDownloadInvoiceClick()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Campaign complete",
            subtitle = "Invoice & final wrap-up",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Celebration Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.5f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), CardShape)
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MyCastNowColors.PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            CheckmarkGlyph(tint = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 3f)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Campaign completed! 🎉",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Nike India · Summer Creator Campaign",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "All deliverables approved · Escrow settled",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.SuccessGreen
                            )
                        }
                    }
                }
            }

            // Summary ledger card
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
                            text = "Campaign ledger summary",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "CREATOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkSecondary)
                                Text(text = uiState.creatorName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkPrimary)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "DELIVERABLES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkSecondary)
                                Text(text = "Reel + 30-day ads", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkPrimary)
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MyCastNowColors.SurfaceDivider))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "TOTAL ESCROW PAID", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkSecondary)
                                Text(text = uiState.totalPaidFormatted, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.PrimaryBlue)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "GST TAX INVOICE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkSecondary)
                                Text(text = uiState.invoiceNumber, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkPrimary)
                            }
                        }
                    }
                }
            }

            // Interactive Rating Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rate your experience with ${uiState.creatorName}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "Your private review helps the platform improve creator rankings.",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        StarRatingBar(
                            rating = uiState.rating,
                            onRatingChanged = { viewModel.onRatingChanged(it) },
                            isEditable = true,
                            starSize = 28.dp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = uiState.ratingLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Dual Bottom Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .border(1.dp, MyCastNowColors.SurfaceBorder)
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.onBookAgainClicked() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyCastNowColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Book ${uiState.creatorName} for next campaign  →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.BackgroundWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            OutlinedButton(
                onClick = { viewModel.onDownloadInvoiceClicked() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MyCastNowColors.PrimaryBlue),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (uiState.isInvoiceDownloaded) "GST Invoice Downloaded ✓" else "Download GST Invoice (PDF)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.PrimaryBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
