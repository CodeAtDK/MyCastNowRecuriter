package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.CompanyProfile
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    fun getCompanyProfile(): Flow<Resource<CompanyProfile>>
    suspend fun updateCompanyProfile(profile: CompanyProfile): Resource<CompanyProfile>
}
