package dev.dhruva.mycastnowrecuriter.data.repository

import dev.dhruva.mycastnowrecuriter.core.AppError
import dev.dhruva.mycastnowrecuriter.core.Resource
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.repository.EscrowRepository
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import dev.dhruva.mycastnowrecuriter.model.StepStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class EscrowRepositoryImpl : EscrowRepository {

    private val timelineStepsState = MutableStateFlow<List<EscrowTimelineStep>>(MockData.escrowSteps)

    override fun getTimelineSteps(campaignId: String): Flow<Resource<List<EscrowTimelineStep>>> = flow {
        emit(Resource.Loading)
        delay(40)
        emit(Resource.Success(timelineStepsState.value))
    }

    override suspend fun lockDeposit(
        campaignId: String,
        creatorId: String,
        totalAmount: Long
    ): Resource<String> {
        delay(120) // Simulate banking gateway latency
        val transactionId = "TXN-NODAL-MCN-${(1000..9999).random()}"
        return Resource.Success(transactionId)
    }

    override suspend fun submitDeliverableReview(
        campaignId: String,
        isApproved: Boolean,
        revisionNotes: String?
    ): Resource<Boolean> {
        delay(100)
        if (isApproved) {
            // Update step 3 to completed, step 4 to in progress
            timelineStepsState.value = timelineStepsState.value.map { step ->
                when (step.stepNumber) {
                    3 -> step.copy(status = StepStatus.COMPLETED)
                    4 -> step.copy(status = StepStatus.IN_PROGRESS)
                    else -> step
                }
            }
        }
        return Resource.Success(isApproved)
    }

    override suspend fun releaseMilestone(campaignId: String, milestoneAmount: Long): Resource<String> {
        delay(120)
        // Mark step 4 as completed
        timelineStepsState.value = timelineStepsState.value.map { step ->
            if (step.stepNumber == 4) step.copy(status = StepStatus.COMPLETED) else step
        }
        val payoutReference = "RBI-PEX-${(100000..999999).random()}"
        return Resource.Success(payoutReference)
    }

    override suspend fun requestPrioritySupport(campaignId: String): Resource<String> {
        delay(60)
        val ticketId = "SUP-${(1000..9999).random()}"
        return Resource.Success(ticketId)
    }
}
