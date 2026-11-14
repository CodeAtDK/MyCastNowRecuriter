package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.ChatRepository
import dev.dhruva.mycastnowrecuriter.domain.repository.CompanyRepository
import dev.dhruva.mycastnowrecuriter.model.CompanyProfile
import dev.dhruva.mycastnowrecuriter.model.Conversation
import kotlinx.coroutines.flow.Flow

class GetConversationsUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(filter: String? = null, query: String? = null): Flow<Resource<List<Conversation>>> {
        return repository.getConversations(filter, query)
    }
}

class SendChatMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(conversationId: String, text: String): Resource<Conversation> {
        return repository.sendMessage(conversationId, text)
    }
}

class GetCompanyProfileUseCase(
    private val repository: CompanyRepository
) {
    operator fun invoke(): Flow<Resource<CompanyProfile>> {
        return repository.getCompanyProfile()
    }
}
