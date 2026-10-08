package dev.dhruva.mycastnowrecuriter.core

/**
 * Domain errors representing failure scenarios across the app.
 */
sealed class AppError(open val message: String) {
    data class NetworkError(override val message: String = "Unable to connect to the server. Please check your connection.") : AppError(message)
    data class ServerError(val code: Int = 500, override val message: String = "Server encountered an error. Please try again later.") : AppError(message)
    data class NotFoundError(override val message: String = "Requested resource was not found.") : AppError(message)
    data class ValidationError(override val message: String = "Invalid data provided.") : AppError(message)
    data class EscrowError(override val message: String = "Escrow transaction could not be completed.") : AppError(message)
    data class UnknownError(override val message: String = "An unexpected error occurred.") : AppError(message)
}
