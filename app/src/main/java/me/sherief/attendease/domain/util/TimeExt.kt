package me.sherief.attendease.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.sherief.attendease.domain.model.HoursAndMinutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration


val currentSystemTimeZone = TimeZone.currentSystemDefault()
val vancouverTimeZone = TimeZone.of("America/Vancouver")

fun Instant.Companion.systemNow(): Instant = Clock.System.now()

fun Instant.toDefaultLocalDate(): LocalDate = toLocalDateTime(TimeZone.currentSystemDefault()).date

fun Instant.toDefaultLocalDateTime(): LocalDateTime =
    toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDate.toEpochMilliseconds(timeZone: TimeZone = vancouverTimeZone): Long {
    return this.atStartOfDayIn(timeZone).toEpochMilliseconds()
}

fun LocalDateTime.toEpochMilliseconds(timeZone: TimeZone = vancouverTimeZone): Long {
    return this.toInstant(timeZone).toEpochMilliseconds()
}

fun Long.toLocalDateTimeWithZone(timeZone: TimeZone = vancouverTimeZone): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
}

infix fun Instant.durationFromEndToStart(instant: Instant): HoursAndMinutes {
    val durationDiff = this.minus(instant)
    val hours = durationDiff.inWholeHours

    val remainingMillis =
        durationDiff.inWholeMilliseconds - hours.toDuration(DurationUnit.HOURS).inWholeMilliseconds

    val minutes = remainingMillis.toDuration(DurationUnit.MILLISECONDS).inWholeMinutes

    return HoursAndMinutes(hours, minutes.toInt())
}

infix fun LocalDateTime.durationFromEndToStart(localDateTime: LocalDateTime): HoursAndMinutes {
    return toInstant(vancouverTimeZone).durationFromEndToStart(
        localDateTime.toInstant(
            vancouverTimeZone
        )
    )
}

fun isDateInRange(date: LocalDate, startDate: LocalDate, endDate: LocalDate): Boolean {
    return (date >= startDate) && (date <= endDate)
}
