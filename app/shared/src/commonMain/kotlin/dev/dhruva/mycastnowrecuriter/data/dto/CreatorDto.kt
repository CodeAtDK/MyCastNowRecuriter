package dev.dhruva.mycastnowrecuriter.data.dto

/**
 * Data Transfer Object ready for REST API / Ktor JSON serialization.
 */
data class CreatorDto(
    val id: String,
    val name: String,
    val role: String,
    val location: String,
    val rating: Double,
    val reviewCount: Int,
    val badge: String? = null,
    val tag: String? = null,
    val followers: String,
    val engagement: String,
    val onTimeRate: String,
    val reach: String,
    val recommendedPackagePrice: String,
    val recommendedPackageDescription: String,
    val availabilityText: String,
    val femaleAudiencePercent: Int,
    val ageGroup: String,
    val topCitiesText: String,
    val imagePath: String? = null
)
