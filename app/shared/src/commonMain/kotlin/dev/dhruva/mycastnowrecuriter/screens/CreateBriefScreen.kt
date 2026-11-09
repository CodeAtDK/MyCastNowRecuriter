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
import dev.dhruva.mycastnowrecuriter.components.CheckmarkGlyph
import dev.dhruva.mycastnowrecuriter.components.RbiEscrowBadge
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.model.Campaign
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.CreateBriefEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.CreateBriefViewModel

@Composable
fun CreateBriefScreen(
    onBackClick: () -> Unit,
    onBriefCreated: (Campaign) -> Unit,
    viewModel: CreateBriefViewModel = remember { AppContainer.createCreateBriefViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CreateBriefEvent.BriefCreatedSuccess -> onBriefCreated(event.campaign)
                is CreateBriefEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Post new brief",
            subtitle = "Ring-fence budget in RBI escrow",
            onBackClick = onBackClick
        )

        // Error message banner
        uiState.errorMessage?.let { error ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MyCastNowColors.ErrorRedTint)
                    .border(1.dp, MyCastNowColors.ErrorRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = error,
                    color = MyCastNowColors.ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Campaign Title Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Campaign title & concept",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MyCastNowColors.SurfaceNeutral)
                            .border(1.dp, MyCastNowColors.SurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        if (uiState.title.isEmpty()) {
                            Text(
                                text = "e.g. Nike Air Max Autumn Athleisure Launch Reel",
                                color = MyCastNowColors.InkLight,
                                fontSize = 13.sp
                            )
                        }
                        BasicTextField(
                            value = uiState.title,
                            onValueChange = { viewModel.onTitleChanged(it) },
                            textStyle = TextStyle(
                                color = MyCastNowColors.InkPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            cursorBrush = SolidColor(MyCastNowColors.PrimaryBlue),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Category Selection
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
                        text = "Primary category",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    val categories = listOf("Fashion & Beauty", "UGC Creators", "Actors & Models", "Fitness", "Tech")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            val isSelected = uiState.selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral)
                                    .border(1.dp, if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(20.dp))
                                    .clickable { viewModel.onCategorySelected(cat) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Deliverables Checklist
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
                        text = "Required deliverables",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    val availableDeliverables = listOf(
                        "1× 4K Instagram Reel",
                        "30-day Meta Ads Whitelisting",
                        "High-Res 5 Photo Edits",
                        "1× Dedicated YouTube Short"
                    )

                    availableDeliverables.forEach { del ->
                        val isChecked = uiState.selectedDeliverables.contains(del)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isChecked) MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.4f) else MyCastNowColors.SurfaceNeutral)
                                .clickable { viewModel.toggleDeliverable(del) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(if (isChecked) MyCastNowColors.PrimaryBlue else MyCastNowColors.BackgroundWhite)
                                    .border(1.dp, if (isChecked) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(5.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    CheckmarkGlyph(modifier = Modifier.size(12.dp), tint = MyCastNowColors.BackgroundWhite)
                                }
                            }
                            Text(
                                text = del,
                                fontSize = 12.sp,
                                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium,
                                color = MyCastNowColors.InkPrimary
                            )
                        }
                    }
                }
            }

            // Budget & Creator Slots Stepper
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
                            text = "Target talent slots",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary,
                            modifier = Modifier.weight(1f, fill = false).padding(end = 6.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MyCastNowColors.SurfaceNeutral)
                                    .border(1.dp, MyCastNowColors.SurfaceBorder, CircleShape)
                                    .clickable { viewModel.onCreatorSlotsChanged(uiState.creatorSlots - 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkPrimary)
                            }

                            Text(
                                text = "${uiState.creatorSlots} Creators",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MyCastNowColors.PrimaryBlueTint)
                                    .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.3f), CircleShape)
                                    .clickable { viewModel.onCreatorSlotsChanged(uiState.creatorSlots + 1) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.PrimaryBlue)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MyCastNowColors.SurfaceDivider)
                    )

                    Text(
                        text = "Budget per creator",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )

                    val budgetChips = listOf(15000L, 25000L, 35000L, 50000L)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(budgetChips) { amount ->
                            val isSelected = uiState.budgetPerCreator == amount
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral)
                                    .border(1.dp, if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.onBudgetPerCreatorChanged(amount) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "₹${amount}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Escrow Summary Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.5f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.3f), CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Estimated Escrow Deposit",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary
                                )
                            }
                            RbiEscrowBadge()
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Base fee (${uiState.creatorSlots} × ₹${uiState.budgetPerCreator})", fontSize = 11.sp, color = MyCastNowColors.InkSecondary)
                            Text("₹${uiState.subtotal}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MyCastNowColors.InkPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (18% Input Tax Credit)", fontSize = 11.sp, color = MyCastNowColors.InkSecondary)
                            Text("₹${uiState.gst}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MyCastNowColors.InkPrimary)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MyCastNowColors.PrimaryBlue.copy(alpha = 0.2f))
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Escrow Protected", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.InkPrimary)
                            Text(uiState.formattedTotal, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.PrimaryBlue)
                        }
                    }
                }
            }

            // Publish CTA Button
            item {
                Button(
                    onClick = { viewModel.submitBrief() },
                    enabled = !uiState.isSubmitting && uiState.isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(color = MyCastNowColors.BackgroundWhite, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Publish Brief & Authorize Escrow (${uiState.formattedTotal})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.BackgroundWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
