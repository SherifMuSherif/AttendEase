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

data class Attendance(
    val attendanceId: String,
    val employeeId: String,
    val date: LocalDate,
    val timeIn: LocalDateTime,
    val timeOut: LocalDateTime,
    private var _totalHoursAndMinutes: HoursAndMinutes = HoursAndMinutes(0, 0)
) {
    init {
        if (!isTimeOutAfterTimeIn()) throw InvalidTimeRangeException("timeOut must be after timeIn.")
        if (_totalHoursAndMinutes == HoursAndMinutes(0, 0))
            _totalHoursAndMinutes = calculateTotalHours()
    }

    val totalHoursAndMinutes: HoursAndMinutes get() = _totalHoursAndMinutes

    private fun calculateTotalHours() = timeOut durationFromEndToStart timeIn
    private fun isTimeOutAfterTimeIn() = (timeOut > timeIn)
}

data class HoursAndMinutes(private var hours: Long, private var minutes: Int) {
    init {
        // If total minutes is 60 or more, convert it to hours
        adjustTimeForMinutesOverflow()
    }

    operator fun plusAssign(other: HoursAndMinutes) {
        this.hours += other.hours
        this.minutes += other.minutes

        // If total minutes is 60 or more, convert it to hours
        adjustTimeForMinutesOverflow()
    }

    private fun adjustTimeForMinutesOverflow() {
        if (this.minutes >= 60) {
            this.minutes -= 60
            this.hours++
        }
    }
}