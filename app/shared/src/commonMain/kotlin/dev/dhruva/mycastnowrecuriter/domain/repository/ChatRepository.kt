package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getConversations(filter: String? = null, query: String? = null): Flow<Resource<List<Conversation>>>
    fun getMessages(conversationId: String): Flow<Resource<List<dev.dhruva.mycastnowrecuriter.model.ChatMessage>>>
    suspend fun sendMessage(conversationId: String, text: String): Resource<Conversation>
    suspend fun sendChatMessage(conversationId: String, text: String): Resource<dev.dhruva.mycastnowrecuriter.model.ChatMessage>
    suspend fun markAsRead(conversationId: String): Resource<Unit>
}
