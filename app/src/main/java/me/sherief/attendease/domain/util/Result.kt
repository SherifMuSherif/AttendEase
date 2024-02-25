package me.sherief.attendease.domain.util

sealed class Result<out T> {

    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()

    val isSuccess get() = this is Success<T>
    val isFailure get() = this is Failure

    companion object {
        fun <T> success(data: T): Result<T> =
            Success(data)

        fun failure(exception: Throwable): Result<Nothing> =
            Failure(exception)
    }
}

/**
 * @return this error if [Result.Failure], null otherwise
 */
fun <R> Result<R>.exceptionOrNull(): Throwable? = when (this) {
    is Result.Failure -> exception
    is Result.Success -> null
}

/**
 * @return this value if [Result.Success], null otherwise
 */
fun <R> Result<R>.getOrNull(): R? = when (this) {
    is Result.Failure -> null
    is Result.Success -> value
}

/**
 * Throws the exception held by [Result.Failure], if `this` is an instance of [Result.Failure].
 */
fun <R> Result<R>.throwOnFailure() {
    if (this is Result.Failure) throw this.exception
}

/**
 * @return the value if `this` is an instance of [Result.Success]
 * @throws Throwable if `this` is an instance of [Result.Failure]
 */
fun <R> Result<R>.getOrThrow(): R = when (this) {
    is Result.Success -> this.value
    is Result.Failure -> throw this.exception
}

/**
 * Shorthand to handle error scenarios depending on the exception received.
 *
 * @param onFailure function to be invoked with the exception as a parameter if `this` is an instance of [Result.Failure]
 *
 * @return `this` data if it is an instance of [Result.Success], the result of [onFailure] otherwise
 */
inline fun <R> Result<R>.getOrElse(onFailure: (exception: Throwable) -> R): R = when (this) {
    is Result.Success -> this.value
    is Result.Failure -> onFailure(this.exception)
}

/**
 * Shorthand to handle error scenarios with a default value.
 *
 * @param defaultValue value to be returned if `this` is an instance of [Result.Failure]
 *
 * @return `this` value if it is an instance of [Result.Success], the [defaultValue] otherwise
 */
fun <R> Result<R>.getOrDefault(defaultValue: R): R = when (this) {
    is Result.Success -> this.value
    is Result.Failure -> defaultValue
}

/**
 * Shorthand to handle error scenarios with a different value as a [Result].
 *
 * @param value to be returned wrapped in a [Result.Success] if `this` is an instance of [Result.Failure]
 *
 * @return `this` if it is an instance of [Result.Success], a [Result.Success] of value otherwise
 */
fun <R> Result<R>.onFailureReturn(value: R): Result<R> = when (this) {
    is Result.Success -> this
    is Result.Failure -> Result.Success(value)
}

/**
 * Call the respective function depending on the instance of `this`.
 *
 * @param onSuccess function to be called if `this` is an instance of [Result.Success]
 * @param onFailure function to be called if `this` is an instance of [Result.Failure]
 *
 * @return the result of the invoked function
 */
inline fun <R> Result<R>.fold(onSuccess: (R) -> R, onFailure: (Throwable) -> R): R = when (this) {
    is Result.Success -> onSuccess(this.value)
    is Result.Failure -> onFailure(this.exception)
}

/**
 * Calls a mapper function with the value of `this` as its parameter if `this` is an instance of [Result.Success].
 *
 * @param transform mapper function to be invoked with `this` value
 *
 * @return the result of [transform] if `this` is an instance of [Result.Success], `this` otherwise
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.success(transform(this.value))
    is Result.Failure -> Result.failure(this.exception)
}


inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
    is Result.Success -> transform(this.getOrThrow())
    is Result.Failure -> Result.failure(this.exception)
}


/**
 * Wraps [block] in a try catch.
 *
 * @return a [Result.Success] with the result of [block] as its value if successful, [Result.Failure] with the exception as its value
 * if an exception was thrown
 */
inline fun <R> doTry(block: () -> R): Result<R> = try {
    Result.Success(block())
} catch (exception: Throwable) {
    Result.Failure(exception)
}

/**
 * Calls a mapper function wrapped by [doTry] with the value of `this` as its parameter if `this`
 * is an instance of [Result.Success].
 *
 * @param fn mapper function to be invoked with `this` value
 *
 * @return the result of [fn] if `this` is an instance of [Result.Success], `this` otherwise
 */
inline fun <T, R> Result<T>.mapCatching(fn: (T) -> R): Result<R> = when (this) {
    is Result.Success -> doTry { fn(value) }
    is Result.Failure -> this
}

/**
 * Calls a mapper function with the value of `this` as its parameter if `this` is an instance of [Result.Failure].
 *
 * @param onFailure mapper function to be invoked with `this` value
 *
 * @return the result of [onFailure] if `this` is an instance of [Result.Failure], `this` otherwise
 */
inline fun <T> Result<T>.mapFailure(onFailure: (Throwable) -> Result<T>): Result<T> =
    when (this) {
        is Result.Success -> this
        is Result.Failure -> onFailure(this.exception)
    }

/**
 * Calls [action] using [also] if `this` is an instance of [Result.Failure].
 *
 * @param action code block to be invoked if `this` is an instance of [Result.Failure], with its error as a parameter
 *
 * @return `this`
 */
inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> = also {
    it.exceptionOrNull()?.let(action)
}

/**
 * Calls [action] using [also] if `this` is an instance of [Result.Success].
 *
 * @param action code block to be invoked if `this` is an instance of [Result.Success], with its value as a parameter
 *
 * @return `this`
 */
inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> = also {
    it.getOrNull()?.let(action)
}

inline fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R> =
    when (this) {
        is Result.Failure -> Result.success(transform(exception))
        is Result.Success -> this
    }

inline fun <R, T : R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> doTry { transform(exception) }
    }
}

inline fun <R, T> Result<T>.flatMapCatching(transform: (value: T) -> Result<R>): Result<R> {
    return when (this) {
        is Result.Failure -> Result.failure(exception)
        is Result.Success -> doTry { transform(value).getOrThrow() }
    }
}

fun <T> T.toResult(): Result<T> = when (this) {
    is Throwable -> Result.failure(this)
    else -> Result.success(this)
}