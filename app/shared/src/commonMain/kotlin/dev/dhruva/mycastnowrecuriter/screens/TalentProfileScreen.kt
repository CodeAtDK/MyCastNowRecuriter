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
import org.jetbrains.compose.resources.painterResource

import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.TalentProfileEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.TalentProfileViewModel

@Composable
fun TalentProfileScreen(
    creator: Creator = MockData.aditiCreator,
    onBackClick: () -> Unit,
    onBookCreatorClick: (Creator, List<DeliverableItem>) -> Unit,
    viewModel: TalentProfileViewModel = remember { AppContainer.createTalentProfileViewModel() },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(creator) {
        viewModel.init(creator)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TalentProfileEvent.ProceedToBooking -> onBookCreatorClick(event.creator, event.selectedDeliverables)
                is TalentProfileEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Talent profile",
            subtitle = "Rate card & audience insights",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Top Header Card
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(106.dp).height(126.dp)) {
                        creator.imageResource?.let { res ->
                            Image(
                                painter = painterResource(res),
                                contentDescription = creator.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Verified tick badge on top right of photo
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MyCastNowColors.PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            CheckmarkGlyph(tint = Color.White, modifier = Modifier.size(11.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = creator.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary,
                                letterSpacing = (-0.3).sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            StatusBadge(
                                text = creator.role,
                                backgroundColor = MyCastNowColors.PrimaryBlueTint,
                                textColor = MyCastNowColors.PrimaryBlue,
                                fontSize = 9
                            )
                            StatusBadge(
                                text = "Pro Verified",
                                backgroundColor = MyCastNowColors.SuccessGreenTint,
                                textColor = MyCastNowColors.SuccessGreen,
                                fontSize = 9
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "📍 ${creator.location}",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "★ ${creator.rating}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.WarningStar
                            )
                            Text(
                                text = "(${creator.reviewCount} verified reviews)",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(MyCastNowColors.SuccessGreen)
                            )
                            Text(
                                text = "Accepting bookings · 3d turnaround",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MyCastNowColors.SuccessGreen
                            )
                        }
                    }
                }
            }

            // 4 Metrics in a row with Trend indicators
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileMetricCard(value = creator.followers, label = "Followers", sub = "+8% MoM", modifier = Modifier.weight(1f))
                    ProfileMetricCard(value = creator.engagement, label = "Engage", sub = "High", modifier = Modifier.weight(1f))
                    ProfileMetricCard(value = creator.onTimeRate, label = "On-time", sub = "Top 1%", modifier = Modifier.weight(1f))
                    ProfileMetricCard(value = creator.reach, label = "Reach", sub = "Monthly", modifier = Modifier.weight(1f))
                }
            }

            // Visual Audience Demographics Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Audience insights",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "Verified data",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gender split with graphical bar
                    Text(
                        text = "Gender breakdown",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.InkSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "👩 ${creator.femaleAudiencePercent}% Women",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PurpleAccent
                        )
                        Text(
                            text = "👨 ${100 - creator.femaleAudiencePercent}% Men",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Dual Segmented Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MyCastNowColors.PrimaryBlue)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(creator.femaleAudiencePercent / 100f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(MyCastNowColors.PurpleAccent)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Age Group Bar
                    Text(
                        text = "Primary Age Group: ${creator.ageGroup} (82% of audience)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.InkSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Top Cities with Visual Progress Bars
                    Text(
                        text = "Top audience locations",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.InkSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CityProgressRow(cityName = "Mumbai", percent = 38)
                    Spacer(modifier = Modifier.height(6.dp))
                    CityProgressRow(cityName = "Delhi NCR", percent = 26)
                    Spacer(modifier = Modifier.height(6.dp))
                    CityProgressRow(cityName = "Bengaluru", percent = 18)
                }
            }

            // Choose deliverables section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Choose deliverables",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "Select scope to customize your booking package",
                            fontSize = 11.sp,
                            color = MyCastNowColors.InkSecondary
                        )
                    }
                    RbiEscrowBadge(text = "Escrow locked")
                }
            }

            // Deliverables Checklist Cards
            items(uiState.deliverables.size) { index ->
                val item = uiState.deliverables[index]
                DeliverableCheckboxRow(
                    item = item,
                    onToggle = {
                        viewModel.onToggleDeliverable(index)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
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
                        text = "TOTAL ESTIMATE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "₹${formatNumber(uiState.totalAmount)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    Text(
                        text = "${uiState.summaryText} · +18% GST",
                        fontSize = 10.sp,
                        color = MyCastNowColors.InkSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = {
                        viewModel.onBookClicked()
                    },
                    enabled = uiState.selectedCount > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyCastNowColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Book ${creator.name.split(" ").firstOrNull() ?: ""}  →",
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
private fun ProfileMetricCard(
    value: String,
    label: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MyCastNowColors.SurfaceNeutral)
            .border(1.dp, MyCastNowColors.SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MyCastNowColors.InkSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                color = MyCastNowColors.PrimaryBlue
            )
        }
    }
}

@Composable
private fun CityProgressRow(
    cityName: String,
    percent: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = cityName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MyCastNowColors.InkPrimary,
            modifier = Modifier.width(76.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MyCastNowColors.SurfaceProgressTrack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(MyCastNowColors.PrimaryBlue)
            )
        }

        Text(
            text = "$percent%",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MyCastNowColors.InkPrimary,
            modifier = Modifier.width(32.dp)
        )
    }
}

@Composable
private fun DeliverableCheckboxRow(
    item: DeliverableItem,
    onToggle: () -> Unit
) {
    val borderColor = if (item.isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder
    val bg = if (item.isSelected) MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.45f) else MyCastNowColors.BackgroundWhite

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(bg)
            .border(1.5.dp, borderColor, CardShape)
            .clickable { onToggle() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checkbox Circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (item.isSelected) MyCastNowColors.PrimaryBlue else Color.Transparent)
                    .border(
                        1.5.dp,
                        if (item.isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (item.isSelected) {
                    CheckmarkGlyph(tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }

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
                color = MyCastNowColors.PrimaryBlue
            )
        }
    }
}

fun formatNumber(num: Long): String {
    val s = num.toString()
    if (s.length <= 3) return s
    val last3 = s.takeLast(3)
    val rest = s.dropLast(3)
    val chunks = StringBuilder()
    for (i in rest.indices.reversed()) {
        chunks.insert(0, rest[i])
        val posFromRight = rest.length - i
        if (posFromRight % 2 == 0 && i != 0) {
            chunks.insert(0, ',')
        }
    }
    return "$chunks,$last3"
}
