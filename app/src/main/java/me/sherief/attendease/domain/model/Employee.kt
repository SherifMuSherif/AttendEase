package me.sherief.attendease.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.util.InvalidTimeRangeException
import me.sherief.attendease.domain.util.durationFromEndToStart

data class Employee(
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val qrCode: String,
    val attendanceHistory: List<Attendance>?
)

/**
 * A class that represents the attendance record of an employee on a given date.
 *
 * @property attendanceId A unique identifier for the attendance record.
 * @property employeeId The employee's ID.
 * @property date The date of the attendance.
 * @property timeIn The time when the employee checked in.
 * @property _timeOut The time when the employee checked out.
 * @property _totalHoursAndMinutes The total hours and minutes worked by the employee on that date, calculated from [timeIn] and [_timeOut].
 * @constructor Initializes the properties of the [Attendance] object and performs validations and calculations.
 * @throws InvalidTimeRangeException If [timeOut] is not after [timeIn].
 */
data class Attendance(
    val attendanceId: String,
    val employeeId: String,
    val date: LocalDate,
    val timeIn: LocalDateTime,
    private var _timeOut: LocalDateTime? = null,
    private var _totalHoursAndMinutes: HoursAndMinutes = HoursAndMinutes(0, 0)
) {
    init {
        if (_timeOut != null) {
            if (isTimeOutAfterTimeIn().not()) throw InvalidTimeRangeException("timeOut must be after timeIn.")
            if (_totalHoursAndMinutes == HoursAndMinutes(0, 0))
                _totalHoursAndMinutes = calculateTotalHours()
        }
    }

    val totalHoursAndMinutes: HoursAndMinutes get() = _totalHoursAndMinutes
    val timeOut: LocalDateTime? get() = _timeOut

    /**
     * Returns the total hours and minutes worked by the employee on that date as a [HoursAndMinutes] object.
     */
    private fun calculateTotalHours() = _timeOut!! durationFromEndToStart timeIn

    /**
     * Checks if a given [targetTimeOut] is after [timeIn] and returns a boolean value.
     *
     * @param targetTimeOut A [LocalDateTime] value that represents the time to compare with [timeIn]. If not specified, it defaults to [_timeOut].
     * @return A boolean value indicating whether [targetTimeOut] is after [timeIn] or not.
     */
    private fun isTimeOutAfterTimeIn(targetTimeOut: LocalDateTime? = _timeOut) =
        (targetTimeOut!! > timeIn)

    /**
     * Updates the [_timeOut] property with a new value and recalculates the [totalHoursAndMinutes] property accordingly.
     *
     * @param newTimeOut A [LocalDateTime] value that represents the new time when the employee checked out.
     * @throws InvalidTimeRangeException If [newTimeOut] is not after [timeIn].
     */
    fun updateTimeOut(newTimeOut: LocalDateTime) {
        if (isTimeOutAfterTimeIn(newTimeOut).not()) throw InvalidTimeRangeException("timeOut must be after timeIn.")
        _timeOut = newTimeOut
        _totalHoursAndMinutes = calculateTotalHours()
    }
}

/**
 * A class that represents a duration of time in hours and minutes.
 *
 * @property hours The number of hours in the duration.
 * @property minutes The number of minutes in the duration.
 * @constructor Initializes the properties of the [HoursAndMinutes] object and adjusts them if [minutes] is 60 or more.
 */
data class HoursAndMinutes(private var hours: Long, private var minutes: Int) {
    init {
        // If total minutes is 60 or more, convert it to hours
        adjustTimeForMinutesOverflow()
    }

    /**
     * Adds another [HoursAndMinutes] object to the current one and assigns the result to the current one.
     */
    operator fun plusAssign(other: HoursAndMinutes) {
        this.hours += other.hours
        this.minutes += other.minutes

        // If total minutes is 60 or more, convert it to hours
        adjustTimeForMinutesOverflow()
    }

    /**
     * Adjusts the [hours] and [minutes] properties if [minutes] is 60 or more by subtracting 60 from [minutes] and adding 1 to [hours].
     */
    private fun adjustTimeForMinutesOverflow() {
        if (this.minutes >= 60) {
            this.minutes -= 60
            this.hours++
        }
    }
}