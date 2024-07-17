package me.sherief.attendease.data.util

import kotlinx.coroutines.Deferred
import me.sherief.attendease.domain.util.ErrorHandler
import me.sherief.attendease.domain.util.Result


suspend fun <T : Any> safeResult(
    errorHandler: ErrorHandler,
    call: suspend () -> T
): Result<T> {
    return try {
        Result.success(call())
    } catch (e: java.lang.Exception) {
        Result.failure(errorHandler.getError(e))
    }
}

suspend fun <T> safeAwaitResult(
    errorHandler: ErrorHandler,
    call: () -> Deferred<T>
): Result<T> {
    return try {
        val result = call().await()
        Result.success(result)
    } catch (exception: Exception) {
        Result.failure(errorHandler.getError(exception))
    }
}

/*
suspend fun <T : Any> safeRequestResult(
    errorHandler: ErrorHandler,
    call: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = call.invoke()

        if (response.code() == 200)
            Result.success((response.body()!!))
        else
            Result.failure(errorHandler.getError(HttpException(response)))

    } catch (e: java.lang.Exception) {
        Result.failure(errorHandler.getError(e))
    }
}*/
