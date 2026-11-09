package dev.dhruva.mycastnowrecuriter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.StatusBadge
import dev.dhruva.mycastnowrecuriter.components.TabAppHeader
import dev.dhruva.mycastnowrecuriter.components.VerifiedGstinBadge
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.CompanyViewModel

@Composable
fun CompanyScreen(
    viewModel: CompanyViewModel = remember { AppContainer.createCompanyViewModel() },
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingClick: (String) -> Unit = {},
    onEscrowLearnMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val company = uiState.profile

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        TabAppHeader(
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )

        // Error banner if any
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
            // Title & Subtitle
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = "Company",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyCastNowColors.InkPrimary,
                        letterSpacing = (-0.4).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your verified brand workspace, team members and escrow billing setup.",
                        fontSize = 12.sp,
                        color = MyCastNowColors.InkSecondary
                    )
                }
            }

            // Brand Profile Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Brand Logo initial capsule
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MyCastNowColors.PrimaryBlueTint)
                                    .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (company.name.length >= 2) company.name.take(2).uppercase() else "CN",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.PrimaryBlue
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = company.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = company.workspaceType,
                                    fontSize = 11.sp,
                                    color = MyCastNowColors.InkSecondary
                                )
                            }

                            VerifiedGstinBadge()
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
                            Text(
                                text = "👥 ${company.teamCount} team  ·  🎬 ${company.liveCampaignsCount} live",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MyCastNowColors.InkSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false).padding(end = 4.dp)
                            )
                            Text(
                                text = "GSTIN: 27AABCN1234F1Z8",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.PrimaryBlue
                            )
                        }
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "Workspace settings & billing",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            }

            // Settings List
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                ) {
                    SettingRow(
                        emoji = "🏢",
                        title = "Company profile",
                        subtitle = "Brand identity, GST registration & billing contact",
                        onClick = {
                            viewModel.onSettingClicked("Company profile")
                            onSettingClick("Company profile")
                        }
                    )
                    SettingDivider()
                    SettingRow(
                        emoji = "👥",
                        title = "Team & access",
                        subtitle = "3 active members · Producer & Approver roles",
                        onClick = {
                            viewModel.onSettingClicked("Team & access")
                            onSettingClick("Team & access")
                        }
                    )
                    SettingDivider()
                    SettingRow(
                        emoji = "🏦",
                        title = "Payments & escrow",
                        subtitle = "RBI nodal accounts, automated invoices & ledger",
                        onClick = {
                            viewModel.onSettingClicked("Payments & escrow")
                            onSettingClick("Payments & escrow")
                        }
                    )
                    SettingDivider()
                    SettingRow(
                        emoji = "🔔",
                        title = "Notifications & SLA alerts",
                        subtitle = "Turnaround alerts, revisions and payout notices",
                        onClick = {
                            viewModel.onSettingClicked("Notifications & SLA alerts")
                            onSettingClick("Notifications & SLA alerts")
                        }
                    )
                    SettingDivider()
                    SettingRow(
                        emoji = "🔒",
                        title = "Security & contracts",
                        subtitle = "2-factor authentication & digital rights transfer",
                        onClick = {
                            viewModel.onSettingClicked("Security & contracts")
                            onSettingClick("Security & contracts")
                        }
                    )
                }
            }

            // Escrow Protection Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.5f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.25f), CardShape)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f).padding(end = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ShieldLockIcon(tint = MyCastNowColors.PrimaryBlue, modifier = Modifier.size(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "100% Escrow Protection",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Payments release only after your deliverable review & signoff.",
                                    fontSize = 11.sp,
                                    color = MyCastNowColors.InkSecondary
                                )
                            }
                        }

                        Text(
                            text = "Learn more  →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.PrimaryBlue,
                            maxLines = 1,
                            modifier = Modifier.clickable {
                                viewModel.onEscrowLearnMoreClicked()
                                onEscrowLearnMoreClick()
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Modal for active setting
    uiState.activeSettingModal?.let { setting ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.onCloseSettingModal() },
            title = {
                Text(
                    text = setting,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (setting) {
                        "Company profile" -> {
                            Text(
                                text = "${company.name}\nGSTIN: 27AABCN1234F1Z8 (Verified Active)\nMumbai, Maharashtra, India\nBilling Contact: accounts@${company.name.lowercase().replace(" ", "")}.com",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                        "Team & access" -> {
                            Text(
                                text = "${company.teamCount} Active Team Members:\n• Sarah K. (Owner / Studio Lead)\n• Vikram R. (Campaign Producer)\n• Priya M. (Finance & Approvals)",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                        "Payments & escrow" -> {
                            Text(
                                text = "Default Method: Corporate HDFC NetBanking (**** 4920)\nEscrow Partner: Axis Bank RBI Nodal Account\nActive Escrow Hold: ₹28,320\nTax Invoice: Automated GST compliant debit advice",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                        "Notifications & SLA alerts" -> {
                            Text(
                                text = "Alerts configured for:\n• 48-hour deliverable deadline warnings\n• Creator draft uploads & script submissions\n• Milestone release confirmation requests",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                        else -> {
                            Text(
                                text = "Two-factor authentication is active on all company administrator logins. Rights transfer deeds are generated upon final milestone disbursement.",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { viewModel.onCloseSettingModal() },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue)
                ) {
                    Text("Done", color = MyCastNowColors.BackgroundWhite)
                }
            }
        )
    }

    // Modal for Escrow Guarantee
    if (uiState.isEscrowInfoModalOpen) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.onCloseEscrowInfoModal() },
            title = {
                Text(
                    text = "100% Escrow Protection Guarantee",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "How MyCastNow protects brand capital:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MyCastNowColors.InkPrimary
                    )
                    Text(
                        text = "• All recruiter deposits are ring-fenced in an RBI-regulated nodal account\n• Creators only receive payouts when you approve deliverables\n• 100% refundable prior to deliverable production\n• Includes formal GST tax invoice ledger and digital copyright assignment upon completion",
                        fontSize = 11.sp,
                        color = MyCastNowColors.InkSecondary,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { viewModel.onCloseEscrowInfoModal() },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlue)
                ) {
                    Text("Understood", color = MyCastNowColors.BackgroundWhite)
                }
            }
        )
    }
}

@Composable
private fun SettingRow(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MyCastNowColors.PrimaryBlueTint),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MyCastNowColors.InkPrimary
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MyCastNowColors.InkSecondary
            )
        }

        Text(
            text = "›",
            fontSize = 20.sp,
            color = MyCastNowColors.InkLight
        )
    }
}

@Composable
private fun SettingDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MyCastNowColors.SurfaceDivider)
    )
}
