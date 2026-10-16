package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.CreatorRepository
import kotlinx.coroutines.flow.Flow

class ToggleBookmarkCreatorUseCase(
    private val repository: CreatorRepository
) {
    operator fun invoke(creatorId: String): Flow<Resource<Boolean>> {
        return repository.toggleBookmarkCreator(creatorId)
    }

    fun observeSavedIds(): Flow<Set<String>> {
        return repository.getSavedCreatorIds()
    }
}
