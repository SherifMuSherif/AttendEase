package me.sherief.attendease.domain.util

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureError] class.
 */
sealed class ErrorEntity(open val failureMessage: String) : Exception() {
    data class NetworkConnection(override val failureMessage: String) : ErrorEntity(failureMessage)
    data class Timeout(override val failureMessage: String) : ErrorEntity(failureMessage)
    data class ServerError(override val failureMessage: String) : ErrorEntity(failureMessage)
    data class UnAuthorized(override val failureMessage: String) : ErrorEntity(failureMessage)
    data class UnKnownError(override val failureMessage: String, val error: Exception? = null) :
        ErrorEntity(failureMessage)

    data class EmptyResponse(override val failureMessage: String) : ErrorEntity(failureMessage)
    data class ManyRequests(
        override val failureMessage: String,
        val retryAfterMsg: String? = null
    ) : ErrorEntity(failureMessage)

    data class CustomError(override val failureMessage: String, val error: Exception? = null) :
        ErrorEntity(failureMessage)


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureError(override val failureMessage: String) : ErrorEntity(failureMessage)
}
