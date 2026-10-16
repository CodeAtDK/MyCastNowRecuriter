package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.ChatRepository
import dev.dhruva.mycastnowrecuriter.domain.repository.NotificationRepository
import dev.dhruva.mycastnowrecuriter.domain.repository.UserRepository
import dev.dhruva.mycastnowrecuriter.model.AppNotification
import dev.dhruva.mycastnowrecuriter.model.ChatMessage
import dev.dhruva.mycastnowrecuriter.model.NotificationCategory
import dev.dhruva.mycastnowrecuriter.model.UserProfile
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = userRepository.getUserProfile()
}

class SwitchWorkspaceUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(workspaceName: String): Resource<UserProfile> =
        userRepository.switchWorkspace(workspaceName)
}

class ToggleTwoFactorUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<UserProfile> =
        userRepository.toggleTwoFactor()
}

class GetNotificationsUseCase(private val notificationRepository: NotificationRepository) {
    operator fun invoke(category: NotificationCategory = NotificationCategory.ALL): Flow<Resource<List<AppNotification>>> =
        notificationRepository.getNotifications(category)
}

class MarkNotificationReadUseCase(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(id: String): Resource<Unit> =
        notificationRepository.markAsRead(id)
}

class GetChatMessagesUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(conversationId: String): Flow<Resource<List<ChatMessage>>> =
        chatRepository.getMessages(conversationId)
}

class SendChatDetailMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(conversationId: String, text: String): Resource<ChatMessage> =
        chatRepository.sendChatMessage(conversationId, text)
}
