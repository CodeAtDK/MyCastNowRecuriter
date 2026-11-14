package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.Creator
import kotlinx.coroutines.flow.Flow

interface CreatorRepository {
    fun getCreators(category: String? = null, query: String? = null): Flow<Resource<List<Creator>>>
    fun getCreatorById(id: String): Flow<Resource<Creator>>
    fun toggleBookmarkCreator(id: String): Flow<Resource<Boolean>>
    fun getSavedCreatorIds(): Flow<Set<String>>
}
