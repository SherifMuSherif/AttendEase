package me.sherief.attendease.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val employeeId: Int = 0,
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val qrCode: String
)
