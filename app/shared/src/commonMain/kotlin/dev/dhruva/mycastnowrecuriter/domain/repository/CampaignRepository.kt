package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.Campaign
import kotlinx.coroutines.flow.Flow

interface CampaignRepository {
    fun getCampaigns(filter: String? = null): Flow<Resource<List<Campaign>>>
    fun getCampaignById(id: String): Flow<Resource<Campaign>>
    suspend fun createBrief(title: String, category: String, budget: Long): Resource<Campaign>
}
