package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.AppError
import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.CampaignRepository
import dev.dhruva.mycastnowrecuriter.model.Campaign
import dev.dhruva.mycastnowrecuriter.model.CampaignStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CampaignRepositoryImpl : CampaignRepository {

    private val campaignsState = MutableStateFlow<List<Campaign>>(MockData.campaigns)

    override fun getCampaigns(filter: String?): Flow<Resource<List<Campaign>>> = flow {
        emit(Resource.Loading)
        delay(60)

        var list = campaignsState.value

        if (!filter.isNullOrBlank()) {
            list = when (filter) {
                "Active 3" -> list.filter { it.status == CampaignStatus.ACTIVE_SHOOT }
                "In review 2" -> list.filter { it.status == CampaignStatus.MILESTONE_PENDING }
                "Drafts 1" -> list.filter { it.status == CampaignStatus.CASTING_OPEN }
                else -> list
            }
        }

        emit(Resource.Success(list))
    }

    override fun getCampaignById(id: String): Flow<Resource<Campaign>> = flow {
        emit(Resource.Loading)
        delay(40)
        val item = campaignsState.value.find { it.id == id }
        if (item != null) {
            emit(Resource.Success(item))
        } else {
            emit(Resource.Error(AppError.NotFoundError("Campaign $id not found")))
        }
    }

    override suspend fun createBrief(title: String, category: String, budget: Long): Resource<Campaign> {
        delay(80)
        val newCampaign = Campaign(
            id = "camp_${campaignsState.value.size + 1}",
            title = title,
            category = category,
            status = CampaignStatus.CASTING_OPEN,
            statusLabel = "CASTING OPEN",
            talentCountText = "0 creators booked",
            budgetFormatted = "₹${budget / 1000}K",
            progressPercent = 10,
            actionLabel = "Review Auditions"
        )
        campaignsState.value = listOf(newCampaign) + campaignsState.value
        return Resource.Success(newCampaign)
    }
}
