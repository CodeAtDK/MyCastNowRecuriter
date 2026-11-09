package dev.dhruva.mycastnowrecuriter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform