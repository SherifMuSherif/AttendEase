package me.sherief.attendease.data.util

import me.sherief.attendease.R
import me.sherief.attendease.domain.util.DomainException
import me.sherief.attendease.domain.util.EmptyRecord
import me.sherief.attendease.domain.util.ErrorHandler
import me.sherief.attendease.domain.util.InvalidTimeRangeException
import javax.inject.Inject


class ErrorHandlerImpl @Inject constructor(private val resourceManager: ResourceManager) :
    ErrorHandler {

    override fun getError(throwable: Throwable): DomainException {
        return when (throwable) {

            is InvalidTimeRangeException -> InvalidTimeRangeException(resourceManager.getString(R.string.invalid_time_range_exception))
            is EmptyRecord -> EmptyRecord(resourceManager.getString(R.string.empty_record_exception))


//            is IOException -> DomainException.NetworkException.NetworkConnection(resourceManager.getString(R.string.your_device_is_not_connect))

            else -> DomainException.CustomException(
                resourceManager.getString(
                    R.string.error_sorry_not_able_to_load,
                    throwable
                )
            )
        }
    }
}