package dev.dhruva.mycastnowrecuriter.viewmodel

import dev.dhruva.mycastnowrecuriter.data.repository.*
import dev.dhruva.mycastnowrecuriter.domain.repository.*
import dev.dhruva.mycastnowrecuriter.domain.usecase.*

/**
 * Dependency container providing single-source repositories, use cases, and ViewModels.
 * Can be swapped directly with Koin modules or Dagger/Hilt when required.
 */
object AppContainer {

    // Repositories (Singletons)
    val creatorRepository: CreatorRepository by lazy { CreatorRepositoryImpl() }
    val campaignRepository: CampaignRepository by lazy { CampaignRepositoryImpl() }
    val escrowRepository: EscrowRepository by lazy { EscrowRepositoryImpl() }
    val chatRepository: ChatRepository by lazy { ChatRepositoryImpl() }
    val companyRepository: CompanyRepository by lazy { CompanyRepositoryImpl() }
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl() }

    // Use Cases
    val getCreatorsUseCase by lazy { GetCreatorsUseCase(creatorRepository) }
    val toggleBookmarkUseCase by lazy { ToggleBookmarkCreatorUseCase(creatorRepository) }
    val getCampaignsUseCase by lazy { GetCampaignsUseCase(campaignRepository) }
    val createCampaignBriefUseCase by lazy { CreateCampaignBriefUseCase(campaignRepository) }
    val getEscrowTimelineUseCase by lazy { GetEscrowTimelineUseCase(escrowRepository) }
    val lockEscrowDepositUseCase by lazy { LockEscrowDepositUseCase(escrowRepository) }
    val submitDeliverableReviewUseCase by lazy { SubmitDeliverableReviewUseCase(escrowRepository) }
    val releaseMilestoneUseCase by lazy { ReleaseMilestoneUseCase(escrowRepository) }
    val requestPrioritySupportUseCase by lazy { RequestPrioritySupportUseCase(escrowRepository) }
    val getConversationsUseCase by lazy { GetConversationsUseCase(chatRepository) }
    val sendChatMessageUseCase by lazy { SendChatMessageUseCase(chatRepository) }
    val getCompanyProfileUseCase by lazy { GetCompanyProfileUseCase(companyRepository) }
    val getUserProfileUseCase by lazy { GetUserProfileUseCase(userRepository) }
    val switchWorkspaceUseCase by lazy { SwitchWorkspaceUseCase(userRepository) }
    val toggleTwoFactorUseCase by lazy { ToggleTwoFactorUseCase(userRepository) }
    val getNotificationsUseCase by lazy { GetNotificationsUseCase(notificationRepository) }
    val markNotificationReadUseCase by lazy { MarkNotificationReadUseCase(notificationRepository) }
    val getChatMessagesUseCase by lazy { GetChatMessagesUseCase(chatRepository) }
    val sendChatDetailMessageUseCase by lazy { SendChatDetailMessageUseCase(chatRepository) }

    // Factory methods for ViewModels
    fun createDiscoverViewModel(): DiscoverViewModel =
        DiscoverViewModel(getCreatorsUseCase, toggleBookmarkUseCase)

    fun createTalentProfileViewModel(): TalentProfileViewModel =
        TalentProfileViewModel(toggleBookmarkUseCase)

    fun createBookingCheckoutViewModel(): BookingCheckoutViewModel =
        BookingCheckoutViewModel(lockEscrowDepositUseCase)

    fun createCampaignsViewModel(): CampaignsViewModel =
        CampaignsViewModel(getCampaignsUseCase, createCampaignBriefUseCase)

    fun createEscrowTrackerViewModel(): EscrowTrackerViewModel =
        EscrowTrackerViewModel(getEscrowTimelineUseCase, requestPrioritySupportUseCase)

    fun createDeliverableReviewViewModel(): DeliverableReviewViewModel =
        DeliverableReviewViewModel(submitDeliverableReviewUseCase)

    fun createMilestoneReleaseViewModel(): MilestoneReleaseViewModel =
        MilestoneReleaseViewModel(releaseMilestoneUseCase)

    fun createCampaignWrapViewModel(): CampaignWrapViewModel =
        CampaignWrapViewModel()

    fun createMessagesViewModel(): MessagesViewModel =
        MessagesViewModel(getConversationsUseCase, sendChatMessageUseCase)

    fun createCompanyViewModel(): CompanyViewModel =
        CompanyViewModel(getCompanyProfileUseCase)

    fun createProfileViewModel(): ProfileViewModel =
        ProfileViewModel(getUserProfileUseCase, switchWorkspaceUseCase, toggleTwoFactorUseCase)

    fun createNotificationsViewModel(): NotificationsViewModel =
        NotificationsViewModel(getNotificationsUseCase, markNotificationReadUseCase)

    fun createCreateBriefViewModel(): CreateBriefViewModel =
        CreateBriefViewModel(createCampaignBriefUseCase)

    fun createChatDetailViewModel(): ChatDetailViewModel =
        ChatDetailViewModel(getChatMessagesUseCase, sendChatDetailMessageUseCase)
}
