package dev.dhruva.mycastnowrecuriter.domain.repository

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import kotlinx.coroutines.flow.Flow

interface EscrowRepository {
    fun getTimelineSteps(campaignId: String): Flow<Resource<List<EscrowTimelineStep>>>
    suspend fun lockDeposit(campaignId: String, creatorId: String, totalAmount: Long): Resource<String>
    suspend fun submitDeliverableReview(campaignId: String, isApproved: Boolean, revisionNotes: String? = null): Resource<Boolean>
    suspend fun releaseMilestone(campaignId: String, milestoneAmount: Long): Resource<String>
    suspend fun requestPrioritySupport(campaignId: String): Resource<String>
}
