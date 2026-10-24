package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetChatMessagesUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.SendChatDetailMessageUseCase
import dev.dhruva.mycastnowrecuriter.model.ChatMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatDetailUiState(
    val conversationId: String = "chat_1",
    val creatorName: String = "Aditi Sharma",
    val campaignTitle: String = "Nike India Summer Reel",
    val messages: List<ChatMessage> = emptyList(),
    val messageInput: String = "",
    val isSending: Boolean = false,
    val isOnline: Boolean = true,
    val errorMessage: String? = null
)

sealed interface ChatDetailEvent {
    data class NavigateToDeliverableReview(val campaignTitle: String) : ChatDetailEvent
    data class ShowMessage(val message: String) : ChatDetailEvent
}

class ChatDetailViewModel(
    private val getMessagesUseCase: GetChatMessagesUseCase,
    private val sendMessageUseCase: SendChatDetailMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChatDetailEvent>()
    val events: SharedFlow<ChatDetailEvent> = _events.asSharedFlow()

    fun init(conversationId: String, creatorName: String, campaignTitle: String) {
        _uiState.update {
            it.copy(
                conversationId = conversationId,
                creatorName = creatorName,
                campaignTitle = campaignTitle
            )
        }
        loadMessages(conversationId)
    }

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(messageInput = text) }
    }

    fun onSendMessage() {
        val text = _uiState.value.messageInput.trim()
        if (text.isBlank()) return

        val convId = _uiState.value.conversationId
        _uiState.update { it.copy(messageInput = "", isSending = true) }

        viewModelScope.launch {
            val res = sendMessageUseCase(convId, text)
            res.onSuccess {
                _uiState.update { it.copy(isSending = false) }
                loadMessages(convId)
            }
            res.onError { err ->
                _uiState.update { it.copy(isSending = false, errorMessage = err.message) }
            }
        }
    }

    fun onDeliverableAttachmentClick() {
        viewModelScope.launch {
            _events.emit(ChatDetailEvent.NavigateToDeliverableReview(_uiState.value.campaignTitle))
        }
    }

    private fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            getMessagesUseCase(conversationId).collect { res ->
                res.onSuccess { list ->
                    _uiState.update { it.copy(messages = list) }
                }
            }
        }
    }
}
