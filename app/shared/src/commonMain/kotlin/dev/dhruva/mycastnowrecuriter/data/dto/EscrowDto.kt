package dev.dhruva.mycastnowrecuriter.data.dto

/**
 * Data Transfer Object for Escrow and production milestones.
 */
data class EscrowTimelineStepDto(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val status: String
)

data class LockDepositRequestDto(
    val campaignId: String,
    val creatorId: String,
    val deliverableIds: List<String>,
    val totalAmount: Long
)

data class LockDepositResponseDto(
    val transactionId: String,
    val status: String,
    val escrowReference: String
)
