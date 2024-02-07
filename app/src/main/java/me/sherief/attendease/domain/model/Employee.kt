package me.sherief.attendease.domain.model

import java.util.Date

data class Employee(
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val qrCode: String,
    val attendanceHistory: List<Attendance>
)

data class Attendance(
    val attendanceId: String,
    val employeeId: String,
    val date: Date,
    val timeIn: Long,
    val timeOut: Long,
    val totalHours: Float
)