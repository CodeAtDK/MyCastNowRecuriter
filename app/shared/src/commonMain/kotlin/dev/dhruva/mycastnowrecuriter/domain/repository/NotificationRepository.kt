package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.AppNotification
import dev.dhruva.mycastnowrecuriter.model.NotificationCategory
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(category: NotificationCategory = NotificationCategory.ALL): Flow<Resource<List<AppNotification>>>
    suspend fun markAsRead(notificationId: String): Resource<Unit>
    suspend fun markAllAsRead(): Resource<Unit>
}
