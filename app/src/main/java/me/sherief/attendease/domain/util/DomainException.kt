package me.sherief.attendease.domain.util

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureException] class.
 */
sealed class DomainException(open val failureMessage: String, throwable: Throwable? = null) :
    Exception(failureMessage, throwable) {
    sealed class NetworkException(
        override val failureMessage: String,
        throwable: Throwable? = null
    ) : DomainException(failureMessage, throwable) {
        data class NetworkConnection(override val failureMessage: String) :
            NetworkException(failureMessage)

        data class Timeout(override val failureMessage: String) : NetworkException(failureMessage)
        data class ServerError(override val failureMessage: String) :
            NetworkException(failureMessage)

        data class UnAuthorized(override val failureMessage: String) :
            NetworkException(failureMessage)

        data class UnKnownError(
            override val failureMessage: String,
            val throwable: Throwable? = null
        ) : NetworkException(failureMessage, throwable)

        data class EmptyResponse(override val failureMessage: String) :
            NetworkException(failureMessage)

        data class ManyRequests(
            override val failureMessage: String,
            val retryAfterMsg: String? = null
        ) : NetworkException(failureMessage)

    }

    data class CustomException(
        override val failureMessage: String,
        val throwable: Throwable? = null
    ) : DomainException(failureMessage, throwable)


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureException(
        override val failureMessage: String,
        throwable: Throwable? = null
    ) : DomainException(failureMessage, throwable)
}

data class EmptyRecord(override val failureMessage: String) :
    DomainException(failureMessage)

data class InvalidTimeRangeException(override val failureMessage: String) :
    DomainException(failureMessage)