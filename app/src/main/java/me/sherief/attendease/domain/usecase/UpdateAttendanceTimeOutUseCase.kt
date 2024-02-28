package me.sherief.attendease.domain.usecase

import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.doTry
import me.sherief.attendease.domain.util.flatMap
import javax.inject.Inject

class UpdateAttendanceTimeOutUseCase @Inject constructor(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(attendanceId: String, timeOut: LocalDateTime): Result<Attendance> =
        doTry {
            attendanceRepository.updateAttendanceTimeOut(attendanceId, timeOut)
        }.flatMap { it }
}