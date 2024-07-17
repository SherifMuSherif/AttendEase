package me.sherief.attendease.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.data.model.AttendanceEntity
import me.sherief.attendease.data.model.EmployeeEntity
import me.sherief.attendease.data.model.EmployeeWithAttendances

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Update
    suspend fun updateEmployee(employee: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)

    @Query("SELECT * FROM EmployeeEntity")
    suspend fun getAllEmployees(): List<EmployeeEntity>

//    @Query("SELECT * FROM EmployeeEntity WHERE employeeId = :employeeId")
//    suspend fun getEmployeeById(employeeId: Int): EmployeeEntity

    @Transaction
    @Query("SELECT * FROM EmployeeEntity WHERE mobile = :employeeMobile")
    suspend fun getEmployeeByMobile(employeeMobile: String): EmployeeWithAttendances

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    @Query("UPDATE AttendanceEntity SET timeOut = :timeOut WHERE  attendanceId = :attendanceId")
    suspend fun updateAttendanceTimeOut(attendanceId: Int, timeOut: LocalDateTime)

    @Transaction
    @Query("SELECT * FROM EmployeeEntity WHERE employeeId = :employeeId")
    suspend fun getEmployeeByIdWithAttendances(employeeId: Int): EmployeeWithAttendances

    @Transaction
    @Query("SELECT * FROM EmployeeEntity")
    suspend fun getAllEmployeesWithAttendances(): List<EmployeeWithAttendances>

}
