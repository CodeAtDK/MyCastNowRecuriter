package dev.dhruva.mycastnowrecuriter.data

import dev.dhruva.mycastnowrecuriter.model.*
import mycastnowrecuriter.app.shared.generated.resources.Res
import mycastnowrecuriter.app.shared.generated.resources.creator_aditi
import mycastnowrecuriter.app.shared.generated.resources.creator_rohan
import mycastnowrecuriter.app.shared.generated.resources.creator_tara

object MockData {
    val aditiCreator = Creator(
        id = "creator_1",
        name = "Aditi Sharma",
        role = "PRO MODEL",
        location = "Fashion · Mumbai",
        tag = "Vogue featured",
        badge = "TOP PRO MODEL",
        rating = 4.98,
        reviewCount = 42,
        isAcceptingBookings = true,
        followers = "420K",
        engagement = "4.9%",
        onTimeRate = "99.4%",
        reach = "1.2M",
        recommendedPackagePrice = "₹24,000",
        recommendedPackageDescription = "Reel + 30-day ads",
        availabilityText = "Mumbai · Available in 3 days",
        femaleAudiencePercent = 72,
        ageGroup = "18–34",
        topCitiesText = "Mumbai 38% · Delhi 26% · Bengaluru 15%",
        imageResource = Res.drawable.creator_aditi
    )

    val rohanCreator = Creator(
        id = "creator_2",
        name = "Rohan Sen",
        role = "UGC · Bengaluru",
        location = "Bengaluru",
        tag = "Sony FX3 specialist",
        badge = "FEATURED UGC",
        rating = 4.92,
        reviewCount = 28,
        isAcceptingBookings = true,
        followers = "185K",
        engagement = "5.4%",
        onTimeRate = "98.8%",
        reach = "650K",
        recommendedPackagePrice = "₹15,000",
        recommendedPackageDescription = "1× Reel",
        availabilityText = "Bengaluru · Available this Friday",
        imageResource = Res.drawable.creator_rohan
    )

    val taraCreator = Creator(
        id = "creator_3",
        name = "Tara Kapoor",
        role = "Actor · New Delhi",
        location = "New Delhi",
        tag = "Theatre & TVC",
        badge = "RISING STAR",
        rating = 5.0,
        reviewCount = 19,
        isAcceptingBookings = true,
        followers = "95K",
        engagement = "6.1%",
        onTimeRate = "100%",
        reach = "380K",
        recommendedPackagePrice = "₹35,000",
        recommendedPackageDescription = "Per shift",
        availabilityText = "Delhi · Available next week",
        imageResource = Res.drawable.creator_tara
    )

    val allCreators = listOf(aditiCreator, rohanCreator, taraCreator)

    val defaultDeliverables = listOf(
        DeliverableItem(
            id = "del_1",
            title = "1× Instagram Reel · 4K",
            description = "Concept, styling and one revision",
            priceFormatted = "₹18,000",
            priceAmount = 18000L,
            isSelected = true,
            isMandatory = true
        ),
        DeliverableItem(
            id = "del_2",
            title = "30-day Meta ads whitelisting",
            description = "Partnership code for brand ads",
            priceFormatted = "₹6,000",
            priceAmount = 6000L,
            isSelected = true,
            isMandatory = false
        ),
        DeliverableItem(
            id = "del_3",
            title = "High-res still photos · 5 edits",
            description = "Studio / outdoor, 48-hour delivery",
            priceFormatted = "+ ₹12,000",
            priceAmount = 12000L,
            isSelected = false,
            isMandatory = false
        )
    )

    val campaigns = listOf(
        Campaign(
            id = "camp_1",
            title = "Nykaa Summer Glow 2025",
            category = "BEAUTY & SKINCARE",
            status = CampaignStatus.ACTIVE_SHOOT,
            statusLabel = "Active shoot",
            talentCountText = "4 booked",
            budgetFormatted = "₹1,20,000",
            progressPercent = 65,
            actionLabel = "Track progress"
        ),
        Campaign(
            id = "camp_2",
            title = "Lakmé Fashion Week TVC",
            category = "COUTURE & TV",
            status = CampaignStatus.MILESTONE_PENDING,
            statusLabel = "Milestone pending",
            talentCountText = "2 talent",
            budgetFormatted = "₹90,000",
            progressPercent = 85,
            actionLabel = "Review milestone"
        ),
        Campaign(
            id = "camp_3",
            title = "Zara Autumn Capsule Reel",
            category = "SOCIAL REEL CAMPAIGN",
            status = CampaignStatus.CASTING_OPEN,
            statusLabel = "Casting open",
            talentCountText = "18 applicants",
            budgetFormatted = "₹60,000",
            progressPercent = 30,
            actionLabel = "Review auditions"
        )
    )

    val escrowSteps = listOf(
        EscrowTimelineStep(
            stepNumber = 1,
            title = "Deposit secured",
            description = "Funds held in RBI-monitored escrow",
            status = StepStatus.COMPLETED
        ),
        EscrowTimelineStep(
            stepNumber = 2,
            title = "Creator in production",
            description = "Draft due in 3 business days",
            status = StepStatus.IN_PROGRESS
        ),
        EscrowTimelineStep(
            stepNumber = 3,
            title = "Review & revisions",
            description = "Approve or request one revision",
            status = StepStatus.UP_NEXT
        ),
        EscrowTimelineStep(
            stepNumber = 4,
            title = "Release payment",
            description = "Funds unlock after sign-off",
            status = StepStatus.UP_NEXT
        )
    )

    val reviewChecklist = listOf(
        ReviewChecklistItem("rc_1", "Matches the approved creative brief", true),
        ReviewChecklistItem("rc_2", "Product and brand are clearly visible", true),
        ReviewChecklistItem("rc_3", "Usage rights match the booking", true)
    )

    val conversations = listOf(
        Conversation(
            id = "chat_1",
            creatorName = "Aditi Sharma",
            campaignTitle = "Nykaa Summer Glow 2025",
            lastMessage = "Draft uploaded — ready for review.",
            timestamp = "10:42 AM",
            unreadCount = 2,
            creatorId = "creator_1"
        ),
        Conversation(
            id = "chat_2",
            creatorName = "Rohan Sen",
            campaignTitle = "Sony FX3 product shoot",
            lastMessage = "Confirmed the shoot slot for Friday.",
            timestamp = "9:18 AM",
            unreadCount = 1,
            creatorId = "creator_2"
        ),
        Conversation(
            id = "chat_3",
            creatorName = "Natasha Patel",
            campaignTitle = "Zara Autumn Capsule Reel",
            lastMessage = "Thanks, I’ve shared my availability.",
            timestamp = "Yesterday",
            unreadCount = 0,
            initial = "N"
        ),
        Conversation(
            id = "chat_4",
            creatorName = "Zara Verma",
            campaignTitle = "Lakmé Fashion Week TVC",
            lastMessage = "The signed usage contract is attached.",
            timestamp = "Yesterday",
            unreadCount = 0,
            initial = "Z"
        )
    )

    val companyProfile = CompanyProfile(
        name = "Northstar Beauty Pvt Ltd",
        workspaceType = "Brand workspace · Mumbai",
        isGstinVerified = true,
        teamCount = 3,
        liveCampaignsCount = 2
    )

    val userProfile = UserProfile(
        id = "usr_sarah_01",
        name = "Sarah Kapoor",
        role = "Studio Lead & Escrow Approver",
        email = "sarah.kapoor@northstar.co",
        phone = "+91 98201 49823",
        companyName = "Northstar Beauty Pvt Ltd",
        disbursementLimitFormatted = "₹1,00,000 / milestone",
        twoFactorEnabled = true,
        avatarInitials = "SK",
        activeWorkspace = "Northstar Beauty (Primary)"
    )

    val mockNotifications = listOf(
        AppNotification(
            id = "notif_1",
            title = "Nike Summer Reel Draft Uploaded",
            description = "Aditi Sharma submitted 4K Video Draft v2. 18 hours remaining to review before auto-payout.",
            timestamp = "12m ago",
            type = NotificationType.DELIVERABLE_SUBMITTED,
            category = NotificationCategory.DELIVERABLES,
            isRead = false,
            actionLabel = "Review Draft",
            targetRoute = "deliverable_review"
        ),
        AppNotification(
            id = "notif_2",
            title = "₹28,320 Escrow Deposit Secured",
            description = "Nodal account funds ring-fenced for Aditi Sharma · Nike India campaign.",
            timestamp = "2h ago",
            type = NotificationType.ESCROW_LOCKED,
            category = NotificationCategory.ESCROW,
            isRead = false,
            actionLabel = "Track Escrow",
            targetRoute = "escrow_tracker"
        ),
        AppNotification(
            id = "notif_3",
            title = "Milestone SLA Warning (48h)",
            description = "Puma Running Reel review window closes tomorrow at 4:00 PM.",
            timestamp = "5h ago",
            type = NotificationType.SLA_DEADLINE,
            category = NotificationCategory.DELIVERABLES,
            isRead = false,
            actionLabel = "Review Now",
            targetRoute = "deliverable_review"
        ),
        AppNotification(
            id = "notif_4",
            title = "₹24,000 Milestone Disbursed",
            description = "Signed digital usage rights deed generated (REC-2026-8320) for Zara Verma TVC.",
            timestamp = "1d ago",
            type = NotificationType.MILESTONE_RELEASED,
            category = NotificationCategory.ESCROW,
            isRead = true,
            actionLabel = "View Receipt",
            targetRoute = "campaign_wrap"
        ),
        AppNotification(
            id = "notif_5",
            title = "3 New Audition Applications",
            description = "Creators applied for 'Levi's Denim Launch' casting call.",
            timestamp = "2d ago",
            type = NotificationType.AUDITION_RECEIVED,
            category = NotificationCategory.AUDITIONS,
            isRead = true,
            actionLabel = "View Talent",
            targetRoute = "discover"
        )
    )

    val mockChatMessages = mapOf(
        "chat_1" to listOf(
            ChatMessage(
                id = "msg_1",
                senderId = "creator_1",
                senderName = "Aditi Sharma",
                text = "Hi Sarah! Excited to work on the Nike Summer Campaign. I have reviewed the storyboard.",
                timestamp = "10:15 AM",
                isFromMe = false
            ),
            ChatMessage(
                id = "msg_2",
                senderId = "usr_sarah_01",
                senderName = "Sarah Kapoor",
                text = "Welcome Aditi! The ₹28,320 deposit is safely locked in RBI escrow. Please make sure the outdoor shots feature morning sunlight.",
                timestamp = "10:22 AM",
                isFromMe = true
            ),
            ChatMessage(
                id = "msg_3",
                senderId = "creator_1",
                senderName = "Aditi Sharma",
                text = "Absolutely! I just wrapped the primary shoot and uploaded the 4K rough draft for your review.",
                timestamp = "10:40 AM",
                isFromMe = false,
                hasAttachment = true,
                attachmentTitle = "Nike Summer Reel Draft v2.mp4",
                attachmentSubtitle = "4K UHD · 0:24 · Ready for Review",
                attachmentActionLabel = "Review Draft"
            ),
            ChatMessage(
                id = "msg_4",
                senderId = "creator_1",
                senderName = "Aditi Sharma",
                text = "Let me know if you need any colour grade adjustments!",
                timestamp = "10:42 AM",
                isFromMe = false
            )
        )
    )
}
