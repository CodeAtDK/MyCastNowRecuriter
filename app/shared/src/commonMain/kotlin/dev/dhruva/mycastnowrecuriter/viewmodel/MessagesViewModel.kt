package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetConversationsUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.SendChatMessageUseCase
import dev.dhruva.mycastnowrecuriter.model.Conversation
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MessagesUiState(
    val conversations: List<Conversation> = emptyList(),
    val selectedFilter: String = "All chats",
    val filters: List<String> = listOf("All chats", "Active bookings", "Auditions"),
    val searchQuery: String = "",
    val activeConversation: Conversation? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface MessagesEvent {
    data class NavigateToDeliverableReview(val conversation: Conversation) : MessagesEvent
    data class NavigateToCreatorProfile(val creatorId: String) : MessagesEvent
    data class ShowMessage(val message: String) : MessagesEvent
}

class MessagesViewModel(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val sendMessageUseCase: SendChatMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState(isLoading = true))
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MessagesEvent>()
    val events: SharedFlow<MessagesEvent> = _events.asSharedFlow()

    init {
        loadConversations()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadConversations()
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadConversations()
    }

    fun onConversationClicked(conversation: Conversation) {
        _uiState.update { it.copy(activeConversation = conversation) }
    }

    fun onCloseConversation() {
        _uiState.update { it.copy(activeConversation = null) }
    }

    fun onSendQuickMessage(text: String) {
        val conv = _uiState.value.activeConversation ?: return
        viewModelScope.launch {
            sendMessageUseCase(conv.id, text).onSuccess { updated ->
                _uiState.update { it.copy(activeConversation = updated) }
                loadConversations()
            }
        }
    }

    fun onReviewDraftClicked(conversation: Conversation) {
        _uiState.update { it.copy(activeConversation = null) }
        viewModelScope.launch {
            _events.emit(MessagesEvent.NavigateToDeliverableReview(conversation))
        }
    }

    fun onViewCreatorProfileClicked(creatorId: String) {
        _uiState.update { it.copy(activeConversation = null) }
        viewModelScope.launch {
            _events.emit(MessagesEvent.NavigateToCreatorProfile(creatorId))
        }
    }

    private fun loadConversations() {
        val q = _uiState.value.searchQuery
        val f = _uiState.value.selectedFilter

        viewModelScope.launch {
            getConversationsUseCase(filter = f, query = q).collect { res ->
                res.onLoading { _uiState.update { it.copy(isLoading = true) } }
                res.onSuccess { list -> _uiState.update { it.copy(isLoading = false, conversations = list) } }
                res.onError { err -> _uiState.update { it.copy(isLoading = false, errorMessage = err.message) } }
            }
        }
    }
}
