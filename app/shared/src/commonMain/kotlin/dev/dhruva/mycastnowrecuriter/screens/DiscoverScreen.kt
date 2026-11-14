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
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.DiscoverEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.DiscoverViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun DiscoverScreen(
    onOpenTalentProfile: (Creator) -> Unit,
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: DiscoverViewModel = remember { AppContainer.createDiscoverViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is DiscoverEvent.NavigateToProfile -> onOpenTalentProfile(event.creator)
                is DiscoverEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        // Top App Header
        TabAppHeader(
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title & Subtitle with crisp styling
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = "Find your next creator",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Verified talent matched directly to your production brief.",
                        fontSize = 12.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }
            }

            // Search Bar & Filter Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
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
                                        text = "Search creators, skills or niche...",
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

                    // Filter slider button
                    val hasFilter = !uiState.talentFilter.isDefault
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (hasFilter) MyCastNowColors.PrimaryBlueTint else MyCastNowColors.SurfaceNeutral)
                            .border(
                                1.dp,
                                if (hasFilter) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.openFilterSheet() },
                        contentAlignment = Alignment.Center
                    ) {
                        FilterSlidersIcon()
                    }
                }
            }

            // Category filter chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.categories) { category ->
                        val cleanCat = category.replace(Regex("[^a-zA-Z &]"), "").trim()
                        val isSel = cleanCat.equals(uiState.selectedCategory, ignoreCase = true) || category == uiState.selectedCategory
                        FilterTabChip(
                            text = category,
                            isSelected = isSel,
                            onClick = { viewModel.onCategorySelected(cleanCat) }
                        )
                    }
                }
            }

            // Featured Creator Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SparkleIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(15.dp))
                        Text(
                            text = "Made for your next brief",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                    }
                    Text(
                        text = "${uiState.creators.size} creators  ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.PrimaryBlue
                    )
                }
            }

            // Error banner if any
            if (uiState.errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(MyCastNowColors.ErrorRedTint)
                            .border(1.dp, MyCastNowColors.ErrorRed.copy(alpha = 0.3f), CardShape)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "Failed to load creators",
                                fontSize = 12.sp,
                                color = MyCastNowColors.ErrorRed
                            )
                            Button(
                                onClick = { viewModel.refresh() },
                                colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.ErrorRed),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Retry", fontSize = 11.sp, color = MyCastNowColors.BackgroundWhite)
                            }
                        }
                    }
                }
            }

            // Featured Hero Creator Card (First from list)
            val featured = uiState.creators.firstOrNull()
            if (featured != null) {
                item {
                    val isSaved = uiState.savedCreatorIds.contains(featured.id)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(MyCastNowColors.BackgroundWhite)
                            .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                            .clickable { viewModel.onCreatorClicked(featured) }
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Creator image and top info
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Creator photo with rating badge overlay
                                Box(modifier = Modifier.width(104.dp).height(126.dp)) {
                                    featured.imageResource?.let { res ->
                                        Image(
                                            painter = painterResource(res),
                                            contentDescription = featured.name,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(14.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    // Rating pill on image
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(6.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MyCastNowColors.InkPrimary.copy(alpha = 0.85f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "★ ${featured.rating} (${featured.reviewCount})",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MyCastNowColors.BackgroundWhite
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        StatusBadge(
                                            text = featured.badge ?: "TOP PRO CREATOR",
                                            backgroundColor = MyCastNowColors.PrimaryBlueTint,
                                            textColor = MyCastNowColors.PrimaryBlue,
                                            fontSize = 9
                                        )

                                        // Bookmark button
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .clickable { viewModel.onToggleBookmark(featured.id) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            BookmarkIcon(isBookmarked = isSaved)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = featured.name,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MyCastNowColors.InkPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        CheckmarkGlyph(
                                            tint = MyCastNowColors.PrimaryBlue,
                                            modifier = Modifier.size(13.dp),
                                            strokeWidth = 2.4f
                                        )
                                    }

                                    Text(
                                        text = "📍 ${featured.location}",
                                        fontSize = 11.sp,
                                        color = MyCastNowColors.InkSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    featured.tag?.let {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "✦ $it",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MyCastNowColors.PrimaryBlue,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Metrics Row in 3 rounded mini cards
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        HeroMetricChip(value = featured.followers, label = "Followers", modifier = Modifier.weight(1f))
                                        HeroMetricChip(value = featured.engagement, label = "Engage", modifier = Modifier.weight(1f))
                                        HeroMetricChip(value = featured.onTimeRate, label = "On-time", modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                            // Subtle Divider
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MyCastNowColors.SurfaceDivider)
                            )

                            // Bottom action strip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MyCastNowColors.SurfaceNeutral)
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Recommended package",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MyCastNowColors.InkSecondary
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = featured.recommendedPackagePrice,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MyCastNowColors.PrimaryBlue
                                        )
                                        Text(
                                            text = "· ${featured.recommendedPackageDescription}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MyCastNowColors.InkSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(MyCastNowColors.SuccessGreen)
                                        )
                                        Text(
                                            text = featured.availabilityText,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MyCastNowColors.SuccessGreen
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.onCreatorClicked(featured) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MyCastNowColors.PrimaryBlue
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "View profile →",
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
            }

            // Section 2: "More talent for you"
            val moreCreators = uiState.creators.drop(1)
            if (moreCreators.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "More talent for you",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                        Text(
                            text = "View all ›",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MyCastNowColors.PrimaryBlue
                        )
                    }
                }

                items(moreCreators, key = { it.id }) { creator ->
                    SecondaryCreatorCard(
                        creator = creator,
                        onViewClick = { viewModel.onCreatorClicked(creator) }
                    )
                }
            } else if (uiState.creators.isEmpty() && !uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(MyCastNowColors.SurfaceNeutral)
                            .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No creators found",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try clearing search or selecting a different category",
                                fontSize = 12.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (uiState.isFilterSheetOpen) {
        TalentFilterDialog(
            filter = uiState.talentFilter,
            onDismiss = { viewModel.closeFilterSheet() },
            onApply = { viewModel.applyFilter(it) },
            onReset = { viewModel.resetFilter() }
        )
    }
}

@Composable
private fun TalentFilterDialog(
    filter: dev.dhruva.mycastnowrecuriter.model.TalentFilter,
    onDismiss: () -> Unit,
    onApply: (dev.dhruva.mycastnowrecuriter.model.TalentFilter) -> Unit,
    onReset: () -> Unit
) {
    var maxBudget by remember { mutableStateOf(filter.maxBudget) }
    var selectedCity by remember { mutableStateOf(filter.selectedCity) }
    var verifiedOnly by remember { mutableStateOf(filter.verifiedOnly) }
    var instantOnly by remember { mutableStateOf(filter.instantBookingOnly) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Creators",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
                Text(
                    text = "Reset",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.PrimaryBlue,
                    modifier = Modifier.clickable { onReset() }
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Budget Limit
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Maximum Budget per Deliverable: ₹${maxBudget}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf(20000L, 35000L, 50000L, 100000L)) { b ->
                            val isSel = maxBudget == b
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral)
                                    .border(1.dp, if (isSel) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(8.dp))
                                    .clickable { maxBudget = b }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (b == 100000L) "All" else "< ₹${b / 1000}k",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                                )
                            }
                        }
                    }
                }

                // City
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Location",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("All Cities", "Mumbai", "Bengaluru", "Delhi")) { city ->
                            val isSel = selectedCity == city
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceNeutral)
                                    .border(1.dp, if (isSel) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedCity = city }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = city,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) MyCastNowColors.BackgroundWhite else MyCastNowColors.InkPrimary
                                )
                            }
                        }
                    }
                }

                // Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Verified GSTIN Only", fontSize = 12.sp, color = MyCastNowColors.InkPrimary)
                    Switch(
                        checked = verifiedOnly,
                        onCheckedChange = { verifiedOnly = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MyCastNowColors.BackgroundWhite,
                            checkedTrackColor = MyCastNowColors.PrimaryBlue
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available This Week (48h)", fontSize = 12.sp, color = MyCastNowColors.InkPrimary)
                    Switch(
                        checked = instantOnly,
                        onCheckedChange = { instantOnly = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MyCastNowColors.BackgroundWhite,
                            checkedTrackColor = MyCastNowColors.PrimaryBlue
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApply(
                        filter.copy(
                            maxBudget = maxBudget,
                            selectedCity = selectedCity,
                            verifiedOnly = verifiedOnly,
                            instantBookingOnly = instantOnly
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Apply Filters", color = MyCastNowColors.BackgroundWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MyCastNowColors.InkSecondary)
            }
        }
    )
}

@Composable
private fun HeroMetricChip(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MyCastNowColors.SurfaceNeutral)
            .border(1.dp, MyCastNowColors.SurfaceBorder, RoundedCornerShape(8.dp))
            .padding(vertical = 5.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                fontSize = 8.sp,
                color = MyCastNowColors.InkSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SecondaryCreatorCard(
    creator: Creator,
    onViewClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(MyCastNowColors.BackgroundWhite)
            .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
            .clickable { onViewClick() }
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
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = creator.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    CheckmarkGlyph(
                        tint = MyCastNowColors.PrimaryBlue,
                        modifier = Modifier.size(11.dp),
                        strokeWidth = 2.2f
                    )
                }

                Text(
                    text = "${creator.role} · ★ ${creator.rating}",
                    fontSize = 11.sp,
                    color = MyCastNowColors.InkSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${creator.recommendedPackagePrice} / Reel",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.PrimaryBlue
                    )
                    Text(
                        text = "· 3d SLA",
                        fontSize = 10.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }
            }

            Button(
                onClick = onViewClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyCastNowColors.PrimaryBlueTint
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "View",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.PrimaryBlue
                )
            }
        }
    }
}
