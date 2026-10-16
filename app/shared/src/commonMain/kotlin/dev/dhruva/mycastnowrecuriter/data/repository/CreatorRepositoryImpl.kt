package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.AppError
import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.CreatorRepository
import dev.dhruva.mycastnowrecuriter.model.Creator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CreatorRepositoryImpl : CreatorRepository {

    private val savedCreatorIdsFlow = MutableStateFlow<Set<String>>(emptySet())

    override fun getCreators(category: String?, query: String?): Flow<Resource<List<Creator>>> = flow {
        emit(Resource.Loading)
        // Simulate network latency
        delay(60)

        var list = MockData.allCreators

        if (!category.isNullOrBlank() && !category.equals("All talent", ignoreCase = true)) {
            val cleanCat = category.replace(Regex("[^a-zA-Z &]"), "").trim()
            list = list.filter {
                it.role.contains(cleanCat, ignoreCase = true) ||
                it.tag?.contains(cleanCat, ignoreCase = true) == true
            }
        }

        if (!query.isNullOrBlank()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.role.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true)
            }
        }

        emit(Resource.Success(list))
    }

    override fun getCreatorById(id: String): Flow<Resource<Creator>> = flow {
        emit(Resource.Loading)
        delay(40)
        val creator = MockData.allCreators.find { it.id == id }
        if (creator != null) {
            emit(Resource.Success(creator))
        } else {
            emit(Resource.Error(AppError.NotFoundError("Creator with ID $id not found")))
        }
    }

    override fun toggleBookmarkCreator(id: String): Flow<Resource<Boolean>> = flow {
        val current = savedCreatorIdsFlow.value
        val isNowSaved = if (current.contains(id)) {
            savedCreatorIdsFlow.value = current - id
            false
        } else {
            savedCreatorIdsFlow.value = current + id
            true
        }
        emit(Resource.Success(isNowSaved))
    }

    override fun getSavedCreatorIds(): Flow<Set<String>> = savedCreatorIdsFlow
}
