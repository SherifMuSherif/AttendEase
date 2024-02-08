package me.sherief.attendease.domain.usecase

import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetTotalHoursPerDayAsStringUseCase {
    operator fun invoke(
        timeIn: Long,
        timeOut: Long
    ): String {
        val diffMillis = timeOut - timeIn
        val duration = diffMillis.toDuration(DurationUnit.MILLISECONDS)
        return duration.toComponents { hours, minutes, seconds, _ ->
            "%02d:%02d".format(hours, minutes, seconds)
        }
    }
}

class GetTotalHoursPerDayUseCase {
    operator fun invoke(
        timeIn: Long,
        timeOut: Long
    ): Float {
        val diffMillis = timeOut - timeIn
        val duration = diffMillis.toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeHours.toFloat()
    }
}
