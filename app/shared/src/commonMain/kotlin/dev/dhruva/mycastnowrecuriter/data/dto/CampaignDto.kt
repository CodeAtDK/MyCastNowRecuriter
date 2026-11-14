package dev.dhruva.mycastnowrecuriter.data.dto

/**
 * Data Transfer Object for Campaign API endpoints.
 */
data class CampaignDto(
    val id: String,
    val title: String,
    val category: String,
    val status: String,
    val statusLabel: String,
    val talentCountText: String,
    val budgetFormatted: String,
    val budgetAmount: Long,
    val progressPercent: Int,
    val actionLabel: String
)
