package dev.dhruva.mycastnowrecuriter.domain.usecase

import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.domain.repository.EscrowRepository
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import kotlinx.coroutines.flow.Flow

class GetEscrowTimelineUseCase(
    private val repository: EscrowRepository
) {
    operator fun invoke(campaignId: String): Flow<Resource<List<EscrowTimelineStep>>> {
        return repository.getTimelineSteps(campaignId)
    }
}

class LockEscrowDepositUseCase(
    private val repository: EscrowRepository
) {
    suspend operator fun invoke(campaignId: String, creatorId: String, totalAmount: Long): Resource<String> {
        return repository.lockDeposit(campaignId, creatorId, totalAmount)
    }
}

class SubmitDeliverableReviewUseCase(
    private val repository: EscrowRepository
) {
    suspend operator fun invoke(campaignId: String, isApproved: Boolean, revisionNotes: String? = null): Resource<Boolean> {
        return repository.submitDeliverableReview(campaignId, isApproved, revisionNotes)
    }
}

class ReleaseMilestoneUseCase(
    private val repository: EscrowRepository
) {
    suspend operator fun invoke(campaignId: String, milestoneAmount: Long): Resource<String> {
        return repository.releaseMilestone(campaignId, milestoneAmount)
    }
}

class RequestPrioritySupportUseCase(
    private val repository: EscrowRepository
) {
    suspend operator fun invoke(campaignId: String): Resource<String> {
        return repository.requestPrioritySupport(campaignId)
    }
}
