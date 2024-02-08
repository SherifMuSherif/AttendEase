package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject


class AddAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val getTotalHoursPerDayUseCase: GetTotalHoursPerDayUseCase
) {
    suspend operator fun invoke(attendance: Attendance): Result<Attendance> {
        val totalHours = getTotalHoursPerDayUseCase(attendance.timeIn, attendance.timeOut)
        val updatedAttendance = attendance.copy(totalHours = totalHours)
        return attendanceRepository.addAttendance(updatedAttendance)

    }
}