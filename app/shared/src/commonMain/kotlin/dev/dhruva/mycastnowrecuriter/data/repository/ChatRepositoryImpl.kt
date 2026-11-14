package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.AppError
import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.ChatRepository
import dev.dhruva.mycastnowrecuriter.model.Conversation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl : ChatRepository {

    private val conversationsState = MutableStateFlow<List<Conversation>>(MockData.conversations)
    private val messagesState = MutableStateFlow<Map<String, List<dev.dhruva.mycastnowrecuriter.model.ChatMessage>>>(MockData.mockChatMessages)

    override fun getConversations(filter: String?, query: String?): Flow<Resource<List<Conversation>>> = flow {
        emit(Resource.Loading)
        delay(40)
        var list = conversationsState.value

        if (!query.isNullOrBlank()) {
            list = list.filter {
                it.creatorName.contains(query, ignoreCase = true) ||
                it.campaignTitle.contains(query, ignoreCase = true) ||
                it.lastMessage.contains(query, ignoreCase = true)
            }
        }

        emit(Resource.Success(list))
    }

    override fun getMessages(conversationId: String): Flow<Resource<List<dev.dhruva.mycastnowrecuriter.model.ChatMessage>>> = flow {
        emit(Resource.Loading)
        delay(40)
        val msgs = messagesState.value[conversationId] ?: MockData.mockChatMessages["chat_1"] ?: emptyList()
        emit(Resource.Success(msgs))
    }

    override suspend fun sendMessage(conversationId: String, text: String): Resource<Conversation> {
        delay(60)
        val list = conversationsState.value.toMutableList()
        val index = list.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            val updated = list[index].copy(
                lastMessage = text,
                timestamp = "Just now",
                unreadCount = 0
            )
            list[index] = updated
            conversationsState.value = list
            return Resource.Success(updated)
        } else {
            return Resource.Error(AppError.NotFoundError("Conversation $conversationId not found"))
        }
    }

    override suspend fun sendChatMessage(conversationId: String, text: String): Resource<dev.dhruva.mycastnowrecuriter.model.ChatMessage> {
        delay(50)
        val newMsg = dev.dhruva.mycastnowrecuriter.model.ChatMessage(
            id = "msg_${kotlin.random.Random.nextInt(1000, 9999)}",
            senderId = "usr_sarah_01",
            senderName = "Sarah Kapoor",
            text = text,
            timestamp = "Just now",
            isFromMe = true
        )
        val currentMap = messagesState.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: MockData.mockChatMessages["chat_1"] ?: emptyList()).toMutableList()
        currentList.add(newMsg)
        currentMap[conversationId] = currentList
        messagesState.value = currentMap

        // Also update conversation snippet
        sendMessage(conversationId, text)
        return Resource.Success(newMsg)
    }

    override suspend fun markAsRead(conversationId: String): Resource<Unit> {
        val list = conversationsState.value.toMutableList()
        val index = list.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            list[index] = list[index].copy(unreadCount = 0)
            conversationsState.value = list
        }
        return Resource.Success(Unit)
    }
}
