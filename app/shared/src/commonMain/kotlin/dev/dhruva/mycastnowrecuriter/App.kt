package dev.dhruva.mycastnowrecuriter

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.dhruva.mycastnowrecuriter.components.BottomNavBar
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.model.Campaign
import dev.dhruva.mycastnowrecuriter.model.Conversation
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.model.DeliverableItem
import dev.dhruva.mycastnowrecuriter.navigation.NavigationTab
import dev.dhruva.mycastnowrecuriter.navigation.Screen
import dev.dhruva.mycastnowrecuriter.screens.*
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowTheme

@Composable
fun App() {
    MyCastNowTheme {
        // Real backstack-based navigation
        val backStack = remember { mutableStateListOf<Screen>(Screen.Discover) }
        val currentScreen = backStack.lastOrNull() ?: Screen.Discover

        var currentTab by remember { mutableStateOf(NavigationTab.DISCOVER) }
        var selectedCreator by remember { mutableStateOf<Creator>(MockData.aditiCreator) }
        var selectedCampaign by remember { mutableStateOf<Campaign>(MockData.campaigns.first()) }
        var selectedDeliverables by remember { mutableStateOf<List<DeliverableItem>>(MockData.defaultDeliverables.filter { it.isSelected }) }
        var selectedConversation by remember { mutableStateOf<Conversation>(MockData.conversations.first()) }

        // Feedback / Dialog states
        var snackbarMessage by remember { mutableStateOf<String?>(null) }
        var showSupportDialog by remember { mutableStateOf(false) }

        fun navigateTo(screen: Screen) {
            backStack.add(screen)
        }

        fun navigateBack() {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }

        fun switchTab(tab: NavigationTab) {
            currentTab = tab
            backStack.clear()
            backStack.add(tab.screen)
        }

        // Keep current tab in sync when backstack pops to a top-level tab screen
        LaunchedEffect(currentScreen) {
            when (currentScreen) {
                Screen.Discover -> currentTab = NavigationTab.DISCOVER
                Screen.Campaigns -> currentTab = NavigationTab.CAMPAIGNS
                Screen.Messages -> currentTab = NavigationTab.MESSAGES
                Screen.Company -> currentTab = NavigationTab.COMPANY
                else -> Unit
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyCastNowColors.BackgroundWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                // Main Application Content Area with Animated Transitions
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (currentScreen) {
                        Screen.Discover -> {
                            DiscoverScreen(
                                onOpenTalentProfile = { creator ->
                                    selectedCreator = creator
                                    navigateTo(Screen.TalentProfile)
                                },
                                onNotificationsClick = {
                                    navigateTo(Screen.Notifications)
                                },
                                onProfileClick = {
                                    navigateTo(Screen.Profile)
                                }
                            )
                        }

                        Screen.TalentProfile -> {
                            TalentProfileScreen(
                                creator = selectedCreator,
                                onBackClick = { navigateBack() },
                                onBookCreatorClick = { creator, deliverables ->
                                    selectedCreator = creator
                                    selectedDeliverables = deliverables
                                    navigateTo(Screen.BookingCheckout)
                                }
                            )
                        }

                        Screen.BookingCheckout -> {
                            BookingCheckoutScreen(
                                creator = selectedCreator,
                                selectedDeliverables = selectedDeliverables,
                                onBackClick = { navigateBack() },
                                onLockEscrowDepositClick = {
                                    snackbarMessage = "₹28,320 deposit secured & locked in RBI escrow"
                                    navigateTo(Screen.EscrowTracker)
                                },
                                onEditScopeClick = {
                                    navigateBack()
                                }
                            )
                        }

                        Screen.Campaigns -> {
                            CampaignsScreen(
                                onOpenEscrowTracker = { campaign ->
                                    selectedCampaign = campaign
                                    navigateTo(Screen.EscrowTracker)
                                },
                                onOpenDeliverableReview = { campaign ->
                                    selectedCampaign = campaign
                                    navigateTo(Screen.DeliverableReview)
                                },
                                onOpenAuditionsReview = {
                                    switchTab(NavigationTab.DISCOVER)
                                },
                                onNewBriefClick = {
                                    navigateTo(Screen.CreateBrief)
                                },
                                onNotificationsClick = {
                                    navigateTo(Screen.Notifications)
                                },
                                onProfileClick = {
                                    navigateTo(Screen.Profile)
                                }
                            )
                        }

                        Screen.EscrowTracker -> {
                            EscrowTrackerScreen(
                                campaignTitle = selectedCampaign.title,
                                onBackClick = { navigateBack() },
                                onOpenCampaignWorkspace = {
                                    navigateTo(Screen.DeliverableReview)
                                },
                                onContactSupportClick = {
                                    showSupportDialog = true
                                }
                            )
                        }

                        Screen.DeliverableReview -> {
                            DeliverableReviewScreen(
                                campaignTitle = "${selectedCampaign.title} · ${selectedCreator.name}",
                                onBackClick = { navigateBack() },
                                onApproveDeliverableClick = {
                                    snackbarMessage = "Deliverable approved by Studio North"
                                    navigateTo(Screen.MilestoneRelease)
                                },
                                onRequestRevisionClick = {
                                    snackbarMessage = "Revision request sent to ${selectedCreator.name}"
                                }
                            )
                        }

                        Screen.MilestoneRelease -> {
                            MilestoneReleaseScreen(
                                onBackClick = { navigateBack() },
                                onConfirmReleaseClick = {
                                    snackbarMessage = "₹24,000 released & usage rights unlocked!"
                                    navigateTo(Screen.CampaignWrap)
                                }
                            )
                        }

                        Screen.CampaignWrap -> {
                            CampaignWrapScreen(
                                onBackClick = { switchTab(NavigationTab.CAMPAIGNS) },
                                onBookAgainClick = {
                                    switchTab(NavigationTab.DISCOVER)
                                },
                                onDownloadInvoiceClick = {
                                    snackbarMessage = "GST Invoice downloaded (INV-2026-8320.pdf)"
                                }
                            )
                        }

                        Screen.Messages -> {
                            MessagesScreen(
                                onOpenConversation = { conv ->
                                    selectedConversation = conv
                                    navigateTo(Screen.ChatDetail)
                                },
                                onOpenDeliverableReview = {
                                    navigateTo(Screen.DeliverableReview)
                                },
                                onOpenCreatorProfile = { creatorId ->
                                    val creator = MockData.allCreators.find { it.id == creatorId } ?: MockData.aditiCreator
                                    selectedCreator = creator
                                    navigateTo(Screen.TalentProfile)
                                },
                                onNotificationsClick = {
                                    navigateTo(Screen.Notifications)
                                },
                                onProfileClick = {
                                    navigateTo(Screen.Profile)
                                }
                            )
                        }

                        Screen.Company -> {
                            CompanyScreen(
                                onNotificationsClick = {
                                    navigateTo(Screen.Notifications)
                                },
                                onProfileClick = {
                                    navigateTo(Screen.Profile)
                                }
                            )
                        }

                        Screen.Profile -> {
                            ProfileScreen(
                                onBackClick = { navigateBack() },
                                onSignOutClick = {
                                    snackbarMessage = "Signed out of brand workspace"
                                    switchTab(NavigationTab.DISCOVER)
                                }
                            )
                        }

                        Screen.Notifications -> {
                            NotificationsScreen(
                                onBackClick = { navigateBack() },
                                onNavigateToRoute = { route ->
                                    when (route) {
                                        "deliverable_review" -> navigateTo(Screen.DeliverableReview)
                                        "escrow_tracker" -> navigateTo(Screen.EscrowTracker)
                                        "campaign_wrap" -> navigateTo(Screen.CampaignWrap)
                                        "discover" -> switchTab(NavigationTab.DISCOVER)
                                        else -> Unit
                                    }
                                }
                            )
                        }

                        Screen.CreateBrief -> {
                            CreateBriefScreen(
                                onBackClick = { navigateBack() },
                                onBriefCreated = { campaign ->
                                    selectedCampaign = campaign
                                    snackbarMessage = "Brief '${campaign.title}' posted & escrow authorized!"
                                    switchTab(NavigationTab.CAMPAIGNS)
                                }
                            )
                        }

                        Screen.ChatDetail -> {
                            ChatDetailScreen(
                                conversationId = selectedConversation.id,
                                creatorName = selectedConversation.creatorName,
                                campaignTitle = selectedConversation.campaignTitle,
                                onBackClick = { navigateBack() },
                                onOpenDeliverableReview = {
                                    navigateTo(Screen.DeliverableReview)
                                },
                                onOpenCreatorProfile = {
                                    val creator = MockData.allCreators.find { it.id == selectedConversation.creatorId } ?: MockData.aditiCreator
                                    selectedCreator = creator
                                    navigateTo(Screen.TalentProfile)
                                }
                            )
                        }
                    }
                }

                // Bottom Navigation Bar is displayed on the 4 primary tabs
                val isTabScreen = currentScreen in listOf(
                    Screen.Discover,
                    Screen.Campaigns,
                    Screen.Messages,
                    Screen.Company
                )

                if (isTabScreen) {
                    BottomNavBar(
                        currentTab = currentTab,
                        onTabSelected = { tab ->
                            switchTab(tab)
                        }
                    )
                }
            }

            // Interactive Escrow Support Dialog
            if (showSupportDialog) {
                AlertDialog(
                    onDismissRequest = { showSupportDialog = false },
                    title = {
                        Text(
                            text = "24/7 Escrow Support",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.InkPrimary
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Booking Ref: MCN-2026-NYK8320",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )
                            Text(
                                text = "Your ₹28,320 funds are held in an RBI-monitored nodal account. If a deliverable fails the agreed SLA or revisions cannot be resolved, full refund protection applies.",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSupportDialog = false
                                snackbarMessage = "Priority support ticket #4812 opened"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue)
                        ) {
                            Text("Request Agent Call", color = MyCastNowColors.BackgroundWhite)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSupportDialog = false }) {
                            Text("Dismiss", color = MyCastNowColors.InkSecondary)
                        }
                    }
                )
            }

            // Toast / Snackbar Notification
            snackbarMessage?.let { msg ->
                LaunchedEffect(msg) {
                    kotlinx.coroutines.delay(2600)
                    snackbarMessage = null
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp, start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MyCastNowColors.InkPrimary.copy(alpha = 0.95f))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = msg,
                        color = MyCastNowColors.BackgroundWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}