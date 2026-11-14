package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<Resource<UserProfile>>
    suspend fun switchWorkspace(workspaceName: String): Resource<UserProfile>
    suspend fun toggleTwoFactor(): Resource<UserProfile>
}
