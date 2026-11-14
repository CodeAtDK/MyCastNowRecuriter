package dev.dhruva.mycastnowrecuriter.model

import org.jetbrains.compose.resources.DrawableResource

data class Creator(
    val id: String,
    val name: String,
    val role: String,
    val location: String,
    val tag: String? = null,
    val badge: String? = null,
    val rating: Double,
    val reviewCount: Int,
    val isAcceptingBookings: Boolean = true,
    val followers: String,
    val engagement: String,
    val onTimeRate: String,
    val reach: String,
    val recommendedPackagePrice: String,
    val recommendedPackageDescription: String,
    val availabilityText: String,
    val femaleAudiencePercent: Int = 72,
    val ageGroup: String = "18–34",
    val topCitiesText: String = "Mumbai 38% · Delhi 26% · Bengaluru 15%",
    val imageResource: DrawableResource? = null
)
