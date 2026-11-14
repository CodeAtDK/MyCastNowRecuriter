package dev.dhruva.mycastnowrecuriter.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import dev.dhruva.mycastnowrecuriter.components.ShieldLockIcon
import dev.dhruva.mycastnowrecuriter.components.SubscreenHeader
import dev.dhruva.mycastnowrecuriter.components.VerifiedGstinBadge
import dev.dhruva.mycastnowrecuriter.theme.CardShape
import dev.dhruva.mycastnowrecuriter.theme.MyCastNowColors
import dev.dhruva.mycastnowrecuriter.viewmodel.AppContainer
import dev.dhruva.mycastnowrecuriter.viewmodel.ProfileEvent
import dev.dhruva.mycastnowrecuriter.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onSignOutClick: () -> Unit = {},
    viewModel: ProfileViewModel = remember { AppContainer.createProfileViewModel() },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileEvent.SignedOut -> onSignOutClick()
                is ProfileEvent.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyCastNowColors.BackgroundWhite)
    ) {
        SubscreenHeader(
            title = "Recruiter profile",
            subtitle = profile.name,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.PrimaryBlueTint.copy(alpha = 0.4f))
                        .border(1.dp, MyCastNowColors.PrimaryBlue.copy(alpha = 0.2f), CardShape)
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Avatar Initial Circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MyCastNowColors.PrimaryBlue)
                                .border(2.dp, MyCastNowColors.BackgroundWhite, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profile.avatarInitials,
                                color = MyCastNowColors.BackgroundWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = profile.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MyCastNowColors.InkPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                VerifiedGstinBadge()
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = profile.role,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MyCastNowColors.PrimaryBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = profile.companyName,
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Active Workspace Switcher Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Active workspace",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = profile.activeWorkspace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                        }

                        Button(
                            onClick = { viewModel.openWorkspaceModal() },
                            colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.PrimaryBlueTint),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Switch ▾",
                                color = MyCastNowColors.PrimaryBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Escrow Authority & Security Section
            item {
                Text(
                    text = "Signing authority & escrow permissions",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(MyCastNowColors.BackgroundWhite)
                        .border(1.dp, MyCastNowColors.SurfaceBorder, CardShape)
                ) {
                    // Authority Limit Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MyCastNowColors.SuccessGreenTint),
                            contentAlignment = Alignment.Center
                        ) {
                            ShieldLockIcon(tint = MyCastNowColors.SuccessGreen, modifier = Modifier.size(18.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Single Signoff Limit",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Authorized for milestone releases up to ${profile.disbursementLimitFormatted}",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }

                        Text(
                            text = "Level 2",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyCastNowColors.SuccessGreen
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MyCastNowColors.SurfaceDivider)
                    )

                    // Two-Factor Authentication Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleTwoFactor() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MyCastNowColors.PrimaryBlueTint),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔑", fontSize = 16.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Two-Factor Authentication",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyCastNowColors.InkPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (profile.twoFactorEnabled) "Hardware token & SMS OTP verified" else "Disabled · Tap to protect funds",
                                fontSize = 11.sp,
                                color = MyCastNowColors.InkSecondary
                            )
                        }

                        Switch(
                            checked = profile.twoFactorEnabled,
                            onCheckedChange = { viewModel.toggleTwoFactor() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MyCastNowColors.BackgroundWhite,
                                checkedTrackColor = MyCastNowColors.PrimaryBlue
                            )
                        )
                    }
                }
            }

            // Contact & Brand Credentials
            item {
                Text(
                    text = "Recruiter credentials",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            }

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
                    CredentialRow(label = "Official Email", value = profile.email)
                    CredentialRow(label = "Mobile", value = profile.phone)
                    CredentialRow(label = "Designation", value = profile.role)
                    CredentialRow(label = "Primary Entity", value = profile.companyName)
                }
            }

            // Sign Out Button
            item {
                OutlinedButton(
                    onClick = { viewModel.openLogoutDialog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MyCastNowColors.ErrorRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MyCastNowColors.ErrorRed.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Sign Out from Workspace",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Workspace Selection Modal
    if (uiState.showWorkspaceModal) {
        AlertDialog(
            onDismissRequest = { viewModel.closeWorkspaceModal() },
            title = {
                Text(
                    text = "Select Brand Workspace",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    profile.availableWorkspaces.forEach { ws ->
                        val isSelected = ws == profile.activeWorkspace
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MyCastNowColors.PrimaryBlueTint else MyCastNowColors.SurfaceNeutral)
                                .border(1.dp, if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.SurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.switchWorkspace(ws) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ws,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MyCastNowColors.PrimaryBlue else MyCastNowColors.InkPrimary
                                )
                                if (isSelected) {
                                    Text("✓ Active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MyCastNowColors.PrimaryBlue)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.closeWorkspaceModal() }) {
                    Text("Cancel", color = MyCastNowColors.InkSecondary)
                }
            }
        )
    }

    // Sign Out Confirmation Dialog
    if (uiState.showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeLogoutDialog() },
            title = {
                Text(
                    text = "Confirm Sign Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyCastNowColors.InkPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to sign out from ${profile.companyName}? Active escrow funds and milestone reviews will remain securely protected.",
                    fontSize = 12.sp,
                    color = MyCastNowColors.InkSecondary,
                    lineHeight = 17.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmSignOut() },
                    colors = ButtonDefaults.buttonColors(containerColor = MyCastNowColors.ErrorRed)
                ) {
                    Text("Sign Out", color = MyCastNowColors.BackgroundWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeLogoutDialog() }) {
                    Text("Cancel", color = MyCastNowColors.InkSecondary)
                }
            }
        )
    }
}

@Composable
private fun CredentialRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = MyCastNowColors.InkSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MyCastNowColors.InkPrimary)
    }
}
