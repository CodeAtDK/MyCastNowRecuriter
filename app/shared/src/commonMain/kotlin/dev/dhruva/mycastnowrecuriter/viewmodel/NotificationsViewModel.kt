package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetNotificationsUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.MarkNotificationReadUseCase
import dev.dhruva.mycastnowrecuriter.model.AppNotification
import dev.dhruva.mycastnowrecuriter.model.NotificationCategory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val notifications: List<AppNotification> = emptyList(),
    val selectedCategory: NotificationCategory = NotificationCategory.ALL,
    val isLoading: Boolean = false,
    val unreadCount: Int = 0,
    val errorMessage: String? = null
)

sealed interface NotificationsEvent {
    data class NavigateTo(val route: String) : NotificationsEvent
    data class ShowMessage(val message: String) : NotificationsEvent
}

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NotificationsEvent>()
    val events: SharedFlow<NotificationsEvent> = _events.asSharedFlow()

    init {
        loadNotifications()
    }

    fun selectCategory(category: NotificationCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadNotifications()
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            markNotificationReadUseCase(notificationId)
            val updated = _uiState.value.notifications.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
            _uiState.update {
                it.copy(
                    notifications = updated,
                    unreadCount = updated.count { n -> !n.isRead }
                )
            }
        }
    }

    fun onNotificationActionClicked(notification: AppNotification) {
        viewModelScope.launch {
            markAsRead(notification.id)
            notification.targetRoute?.let { route ->
                _events.emit(NotificationsEvent.NavigateTo(route))
            }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            getNotificationsUseCase(_uiState.value.selectedCategory).collect { res ->
                res.onLoading { _uiState.update { it.copy(isLoading = true) } }
                res.onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = list,
                            unreadCount = list.count { n -> !n.isRead }
                        )
                    }
                }
                res.onError { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
            }
        }
    }
}
