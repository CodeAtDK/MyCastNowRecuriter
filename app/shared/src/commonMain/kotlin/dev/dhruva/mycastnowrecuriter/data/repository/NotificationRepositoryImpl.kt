package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.NotificationRepository
import dev.dhruva.mycastnowrecuriter.model.AppNotification
import dev.dhruva.mycastnowrecuriter.model.NotificationCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl : NotificationRepository {

    private val notificationsState = MutableStateFlow<List<AppNotification>>(MockData.mockNotifications)

    override fun getNotifications(category: NotificationCategory): Flow<Resource<List<AppNotification>>> = flow {
        emit(Resource.Loading)
        delay(40)
        val all = notificationsState.value
        val filtered = when (category) {
            NotificationCategory.ALL -> all
            NotificationCategory.ESCROW -> all.filter { it.category == NotificationCategory.ESCROW }
            NotificationCategory.DELIVERABLES -> all.filter { it.category == NotificationCategory.DELIVERABLES }
            NotificationCategory.AUDITIONS -> all.filter { it.category == NotificationCategory.AUDITIONS }
        }
        emit(Resource.Success(filtered))
    }

    override suspend fun markAsRead(notificationId: String): Resource<Unit> {
        val list = notificationsState.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        notificationsState.value = list
        return Resource.Success(Unit)
    }

    override suspend fun markAllAsRead(): Resource<Unit> {
        val list = notificationsState.value.map { it.copy(isRead = true) }
        notificationsState.value = list
        return Resource.Success(Unit)
    }
}
