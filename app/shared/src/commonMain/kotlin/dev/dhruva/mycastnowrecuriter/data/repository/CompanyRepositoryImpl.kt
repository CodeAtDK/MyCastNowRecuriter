package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.CompanyRepository
import dev.dhruva.mycastnowrecuriter.model.CompanyProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class CompanyRepositoryImpl : CompanyRepository {

    private val profileState = MutableStateFlow(MockData.companyProfile)

    override fun getCompanyProfile(): Flow<Resource<CompanyProfile>> = flow {
        emit(Resource.Loading)
        delay(30)
        emit(Resource.Success(profileState.value))
    }

    override suspend fun updateCompanyProfile(profile: CompanyProfile): Resource<CompanyProfile> {
        delay(50)
        profileState.value = profile
        return Resource.Success(profile)
    }
}
