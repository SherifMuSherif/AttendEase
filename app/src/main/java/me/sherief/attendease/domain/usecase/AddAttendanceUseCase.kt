package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.doTry
import me.sherief.attendease.domain.util.flatMap
import javax.inject.Inject


class AddAttendanceUseCase @Inject constructor(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(attendance: Attendance): Result<Attendance> =
      attendanceRepository.addAttendance(attendance)

    suspend operator fun invoke(attendanceCreation: () -> Attendance): Result<Attendance> =
        doTry {
            attendanceCreation()
        }.flatMap {
            attendanceRepository.addAttendance(it)
        }
}