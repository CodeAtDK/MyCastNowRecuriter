package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.*
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.model.DeliverableItem
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.BookingCheckoutEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.BookingCheckoutViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookingCheckoutScreen(
    creator: Creator = MockData.aditiCreator,
    selectedDeliverables: List<DeliverableItem> = MockData.defaultDeliverables.filter { it.isSelected },
    onBackClick: () -> Unit,
    onLockEscrowDepositClick: () -> Unit,
    onEditScopeClick: () -> Unit = {},
    viewModel: BookingCheckoutViewModel = remember { AppContainer.createBookingCheckoutViewModel() },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(creator, selectedDeliverables) {
        viewModel.init(creator, selectedDeliverables)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is BookingCheckoutEvent.EscrowLockedSuccess -> onLockEscrowDepositClick()
                is BookingCheckoutEvent.NavigateBackToScope -> onEditScopeClick()
                is BookingCheckoutEvent.ShowError -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Booking & escrow",
            subtitle = "Protected brand-to-creator payment",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Step and protection badges
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(
                        text = "STEP 2 OF 3 · ESCROW SETUP",
                        backgroundColor = MyCastNowColors.PrimaryBlueTint,
                        textColor = MyCastNowColors.PrimaryBlue
                    )
                    RbiEscrowBadge(text = "RBI Nodal")
                }
            }

            // Creator summary card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        creator.imageResource?.let { res ->
                            Image(
                                painter = painterResource(res),
                                contentDescription = creator.name,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = creator.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                CheckmarkGlyph(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(12.dp))
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${creator.followers} followers · ${creator.location}",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = "Edit scope",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { viewModel.onEditScopeClicked() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Booking scope line items card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Agreed deliverables scope",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )

                        uiState.deliverables.forEachIndexed { index, item ->
                            if (index > 0) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MyCastNowColors.SurfaceDivider)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MyCastNowColors.InkPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.description,
                                        fontSize = 11.sp,
                                        color = MyCastNowColors.InkSecondary
                                    )
                                }
                                Text(
                                    text = item.priceFormatted,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Visual Escrow payment schedule pipeline
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.45f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), CardShape)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(15.dp))
                        Text(
                            text = "How your escrow deposit works",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CheckoutEscrowStepRow(
                        stepNum = "1",
                        title = "Funds locked today in RBI nodal account",
                        desc = "Money stays completely safe; creator is notified that budget is locked."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CheckoutEscrowStepRow(
                        stepNum = "2",
                        title = "Creator produces & submits draft",
                        desc = "Turnaround in 3 business days; includes 1 round of revisions."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CheckoutEscrowStepRow(
                        stepNum = "3",
                        title = "Released only upon your approval",
                        desc = "Usage rights & tax invoice instantly transferred to your brand."
                    )
                }
            }

            // Payment summary ledger card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Payment summary",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Creator talent fee",
                            fontSize = 12.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                        Text(
                            text = "₹${formatNumber(uiState.talentFee)}.00",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MyCastNowColors.InkPrimary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "GST (18% input credit eligible)",
                            fontSize = 12.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                        Text(
                            text = "₹${formatNumber(uiState.gstAmount)}.00",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MyCastNowColors.InkPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MyCastNowColors.SurfaceDivider)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total escrow deposit",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Text(
                                text = "GST invoice issued automatically",
                                fontSize = 10.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }
                        Text(
                            text = "₹${formatNumber(uiState.totalDeposit)}.00",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Bottom Sticky Action Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyCastNowColors.BackgroundWhite)
                .border(1.dp, MyCastNowColors.SurfaceBorder)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                ) {
                    Text(
                        text = "₹${formatNumber(uiState.totalDeposit)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    Text(
                        text = "100% refundable before draft",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.SuccessGreen,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = { viewModel.onLockDepositClicked() },
                    enabled = !uiState.isProcessing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyCastNowColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (uiState.isProcessing) "Locking escrow..." else "Lock escrow deposit →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.BackgroundWhite,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckoutEscrowStepRow(
    stepNum: String,
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
                .background(MyCastNowColors.PrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNum,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = desc,
                fontSize = 10.sp,
                color = MyCastNowColors.InkSecondary
            )
        }
    }
}
