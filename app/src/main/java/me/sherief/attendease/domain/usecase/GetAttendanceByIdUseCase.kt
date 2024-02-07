package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject

class GetAttendanceByIdUseCase @Inject constructor(private val attendanceRepository: AttendanceRepository) {
    suspend operator fun invoke(id: String): Result<Attendance> {
        return attendanceRepository.getAttendanceById(id)
    }
}