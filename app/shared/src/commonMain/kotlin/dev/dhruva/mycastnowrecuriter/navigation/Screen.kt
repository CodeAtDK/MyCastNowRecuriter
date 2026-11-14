package dev.dhruva.mycastnowrecuriter.navigation

sealed class Screen(val route: String) {
    // 4 Bottom Navigation Tabs
    data object Discover : Screen("discover")
    data object Campaigns : Screen("campaigns")
    data object Messages : Screen("messages")
    data object Company : Screen("company")

    // Flow / Detail Screens
    data object TalentProfile : Screen("talent_profile")
    data object BookingCheckout : Screen("booking_checkout")
    data object EscrowTracker : Screen("escrow_tracker")
    data object DeliverableReview : Screen("deliverable_review")
    data object MilestoneRelease : Screen("milestone_release")
    data object CampaignWrap : Screen("campaign_wrap")
    data object Profile : Screen("profile")
    data object Notifications : Screen("notifications")
    data object CreateBrief : Screen("create_brief")
    data object ChatDetail : Screen("chat_detail")
}

enum class NavigationTab(val title: String, val screen: Screen) {
    DISCOVER("Discover", Screen.Discover),
    CAMPAIGNS("Campaigns", Screen.Campaigns),
    MESSAGES("Messages", Screen.Messages),
    COMPANY("Company", Screen.Company)
}
