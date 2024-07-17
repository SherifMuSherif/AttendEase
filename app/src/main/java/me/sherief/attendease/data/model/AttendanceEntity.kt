package me.sherief.attendease.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val attendanceId: Int = 0,
    val attendanceEmployeeId: Int,
    val date: LocalDate,
    val timeIn: LocalDateTime,
    val timeOut: LocalDateTime? = null,
    val totalHours: Long,
    val totalMinutes: Int,
)