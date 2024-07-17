package me.sherief.attendease.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class EmployeeWithAttendances(
    @Embedded val employee: EmployeeEntity,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "attendanceEmployeeId",
    )
    val attendanceRecords: List<AttendanceEntity> = emptyList()
)