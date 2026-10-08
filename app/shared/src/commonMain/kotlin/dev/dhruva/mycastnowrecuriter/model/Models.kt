package dev.dhruva.mycastnowrecuriter.model

enum class CampaignStatus {
    ACTIVE_SHOOT,
    MILESTONE_PENDING,
    CASTING_OPEN,
    COMPLETED
}

data class Campaign(
    val id: String,
    val title: String,
    val category: String,
    val status: CampaignStatus,
    val statusLabel: String,
    val talentCountText: String,
    val budgetFormatted: String,
    val progressPercent: Int,
    val actionLabel: String
)

data class DeliverableItem(
    val id: String,
    val title: String,
    val description: String,
    val priceFormatted: String,
    val priceAmount: Long,
    val isSelected: Boolean = false,
    val isMandatory: Boolean = false
)

enum class StepStatus {
    COMPLETED,
    IN_PROGRESS,
    UP_NEXT
}

data class EscrowTimelineStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val status: StepStatus
)

data class ReviewChecklistItem(
    val id: String,
    val title: String,
    val isChecked: Boolean = true
)

data class Conversation(
    val id: String,
    val creatorName: String,
    val campaignTitle: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val initial: String = "",
    val creatorId: String = ""
)

data class CompanyProfile(
    val name: String = "Northstar Beauty Pvt Ltd",
    val workspaceType: String = "Brand workspace · Mumbai",
    val isGstinVerified: Boolean = true,
    val teamCount: Int = 3,
    val liveCampaignsCount: Int = 2
)

data class UserProfile(
    val id: String = "usr_sarah_01",
    val name: String = "Sarah Kapoor",
    val role: String = "Studio Lead & Escrow Approver",
    val email: String = "sarah.kapoor@northstar.co",
    val phone: String = "+91 98201 49823",
    val companyName: String = "Northstar Beauty Pvt Ltd",
    val disbursementLimitFormatted: String = "₹1,00,000 / milestone",
    val twoFactorEnabled: Boolean = true,
    val avatarInitials: String = "SK",
    val activeWorkspace: String = "Northstar Beauty (Primary)",
    val availableWorkspaces: List<String> = listOf(
        "Northstar Beauty (Primary)",
        "Nike India Studio",
        "Personal Creator Scouting"
    )
)

enum class NotificationCategory {
    ALL,
    ESCROW,
    DELIVERABLES,
    AUDITIONS
}

enum class NotificationType {
    ESCROW_LOCKED,
    SLA_DEADLINE,
    DELIVERABLE_SUBMITTED,
    MILESTONE_RELEASED,
    AUDITION_RECEIVED
}

data class AppNotification(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val type: NotificationType,
    val category: NotificationCategory,
    val isRead: Boolean = false,
    val actionLabel: String? = null,
    val targetRoute: String? = null
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val hasAttachment: Boolean = false,
    val attachmentTitle: String? = null,
    val attachmentSubtitle: String? = null,
    val attachmentActionLabel: String? = null
)

data class TalentFilter(
    val minBudget: Long = 10000,
    val maxBudget: Long = 100000,
    val minEngagement: Float = 0f,
    val selectedTier: String = "All",
    val selectedCity: String = "All Cities",
    val verifiedOnly: Boolean = false,
    val instantBookingOnly: Boolean = false
) {
    val isDefault: Boolean
        get() = minBudget == 10000L && maxBudget == 100000L && minEngagement == 0f &&
                selectedTier == "All" && selectedCity == "All Cities" && !verifiedOnly && !instantBookingOnly
}

