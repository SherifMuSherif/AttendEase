package me.sherief.attendease.domain.util

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: ErrorEntity) : Result<Nothing>()

    val isSuccess get() = this is Success<T>
    val isError get() = this is Error

    companion object {
        fun <T> success(data: T): Result<T> =
            Success(data)

        fun error(exception: ErrorEntity): Result<Nothing> =
            Error(exception)
    }
}