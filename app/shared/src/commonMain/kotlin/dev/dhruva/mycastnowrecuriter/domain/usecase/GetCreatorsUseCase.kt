package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.CreatorRepository
import dev.dhruva.mycastnowrecuriter.model.Creator
import kotlinx.coroutines.flow.Flow

class GetCreatorsUseCase(
    private val repository: CreatorRepository
) {
    operator fun invoke(category: String? = null, query: String? = null): Flow<Resource<List<Creator>>> {
        return repository.getCreators(category, query)
    }
}
