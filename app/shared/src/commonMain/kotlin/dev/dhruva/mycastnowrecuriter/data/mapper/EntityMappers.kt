package dev.dhruva.mycastnowrecuriter.data.mapper

import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.data.dto.CampaignDto
import dev.dhruva.mycastnowrecuriter.data.dto.CreatorDto
import dev.dhruva.mycastnowrecuriter.data.dto.EscrowTimelineStepDto
import dev.dhruva.mycastnowrecuriter.model.Campaign
import dev.dhruva.mycastnowrecuriter.model.CampaignStatus
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import dev.dhruva.mycastnowrecuriter.model.StepStatus

fun CreatorDto.toDomain(): Creator {
    val matchingResource = when (id) {
        "creator_1" -> MockData.aditiCreator.imageResource
        "creator_2" -> MockData.rohanCreator.imageResource
        "creator_3" -> MockData.taraCreator.imageResource
        else -> null
    }

    return Creator(
        id = id,
        name = name,
        role = role,
        location = location,
        rating = rating,
        reviewCount = reviewCount,
        badge = badge,
        tag = tag,
        followers = followers,
        engagement = engagement,
        onTimeRate = onTimeRate,
        reach = reach,
        recommendedPackagePrice = recommendedPackagePrice,
        recommendedPackageDescription = recommendedPackageDescription,
        availabilityText = availabilityText,
        femaleAudiencePercent = femaleAudiencePercent,
        ageGroup = ageGroup,
        topCitiesText = topCitiesText,
        imageResource = matchingResource
    )
}

fun CampaignDto.toDomain(): Campaign {
    val domainStatus = when (status.uppercase()) {
        "ACTIVE_SHOOT" -> CampaignStatus.ACTIVE_SHOOT
        "MILESTONE_PENDING" -> CampaignStatus.MILESTONE_PENDING
        "CASTING_OPEN" -> CampaignStatus.CASTING_OPEN
        "COMPLETED" -> CampaignStatus.COMPLETED
        else -> CampaignStatus.CASTING_OPEN
    }

    return Campaign(
        id = id,
        title = title,
        category = category,
        status = domainStatus,
        statusLabel = statusLabel,
        talentCountText = talentCountText,
        budgetFormatted = budgetFormatted,
        progressPercent = progressPercent,
        actionLabel = actionLabel
    )
}

fun EscrowTimelineStepDto.toDomain(): EscrowTimelineStep {
    val stepStatus = when (status.uppercase()) {
        "COMPLETED" -> StepStatus.COMPLETED
        "IN_PROGRESS" -> StepStatus.IN_PROGRESS
        else -> StepStatus.UP_NEXT
    }

    return EscrowTimelineStep(
        stepNumber = stepNumber,
        title = title,
        description = description,
        status = stepStatus
    )
}
