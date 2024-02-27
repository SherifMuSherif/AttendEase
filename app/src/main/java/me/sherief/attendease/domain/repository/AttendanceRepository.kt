package me.sherief.attendease.domain.repository

import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.util.Result

interface AttendanceRepository {
    suspend fun getAttendanceById(id: String): Result<Attendance>
    suspend fun getAllAttendances(): Result<List<Attendance>>
    suspend fun getAttendancesByEmployeeId(employeeId: String): Result<List<Attendance>>
    suspend fun addAttendance(attendance: Attendance): Result<Attendance>
    suspend fun updateAttendanceTimeOut(
        attendanceId: String,
        timeOut: LocalDateTime
    ): Result<Attendance>

}