package me.sherief.attendease.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.sherief.attendease.data.model.AttendanceEntity
import me.sherief.attendease.data.model.EmployeeEntity

@Database(
    entities = [EmployeeEntity::class, AttendanceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeConverter::class,
    LocalDateConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    abstract fun attendanceDao(): AttendanceDao
}