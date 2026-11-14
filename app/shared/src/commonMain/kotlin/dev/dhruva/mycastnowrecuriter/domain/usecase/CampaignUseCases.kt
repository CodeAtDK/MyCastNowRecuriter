package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.CampaignRepository
import dev.dhruva.mycastnowrecuriter.model.Campaign
import kotlinx.coroutines.flow.Flow

class GetCampaignsUseCase(
    private val repository: CampaignRepository
) {
    operator fun invoke(filter: String? = null): Flow<Resource<List<Campaign>>> {
        return repository.getCampaigns(filter)
    }
}

class CreateCampaignBriefUseCase(
    private val repository: CampaignRepository
) {
    suspend operator fun invoke(title: String, category: String, budget: Long): Resource<Campaign> {
        return repository.createBrief(title, category, budget)
    }
}
