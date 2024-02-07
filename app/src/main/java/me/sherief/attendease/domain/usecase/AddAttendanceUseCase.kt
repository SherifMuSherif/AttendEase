package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject


class AddAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val updateAttendanceUseCase: UpdateAttendanceUseCase
) {
    suspend operator fun invoke(attendance: Attendance): Result<Any> {
        return if (attendance.totalHours == 0f) {
            updateAttendanceUseCase(attendance)
        } else {
            attendanceRepository.addAttendance(attendance)
        }
    }
}