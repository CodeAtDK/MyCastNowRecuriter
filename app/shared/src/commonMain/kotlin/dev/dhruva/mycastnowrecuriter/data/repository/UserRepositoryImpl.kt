package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.UserRepository
import dev.dhruva.mycastnowrecuriter.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl : UserRepository {

    private val userProfileState = MutableStateFlow<UserProfile>(MockData.userProfile)

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading)
        delay(30)
        emit(Resource.Success(userProfileState.value))
    }

    override suspend fun switchWorkspace(workspaceName: String): Resource<UserProfile> {
        delay(60)
        val updated = userProfileState.value.copy(activeWorkspace = workspaceName)
        userProfileState.value = updated
        return Resource.Success(updated)
    }

    override suspend fun toggleTwoFactor(): Resource<UserProfile> {
        delay(60)
        val updated = userProfileState.value.copy(twoFactorEnabled = !userProfileState.value.twoFactorEnabled)
        userProfileState.value = updated
        return Resource.Success(updated)
    }
}
