package me.sherief.attendease.domain.repository

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.util.Result

interface AttendanceRepository {
    suspend fun getAttendanceById(id: String): Result<Attendance>
    suspend fun getAllAttendances(): Result<List<Attendance>>
    suspend fun getAttendancesByEmployeeId(employeeId: String): Result<List<Attendance>>
    suspend fun addAttendance(attendance: Attendance): Result<Any>
    suspend fun updateAttendance(attendance: Attendance): Result<Attendance>

}