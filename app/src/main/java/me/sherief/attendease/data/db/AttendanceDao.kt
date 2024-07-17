package me.sherief.attendease.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.data.model.AttendanceEntity

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM AttendanceEntity")
    suspend fun getAllAttendances(): List<AttendanceEntity>

    @Query("SELECT * FROM AttendanceEntity WHERE attendanceEmployeeId = :employeeId")
    suspend fun getAttendanceByEmployeeId(employeeId: Int): List<AttendanceEntity>

    @Query("SELECT * FROM AttendanceEntity WHERE attendanceEmployeeId = :employeeId AND attendanceId = :attendanceId")
    suspend fun getAttendanceById(employeeId: Int, attendanceId: Int): AttendanceEntity

    @Query("SELECT * FROM AttendanceEntity WHERE attendanceId = :attendanceId")
    suspend fun getAttendanceById(attendanceId: Int): AttendanceEntity

    @Query("UPDATE AttendanceEntity SET timeOut = :timeOut WHERE  attendanceId = :attendanceId")
    suspend fun updateAttendanceTimeOut(attendanceId: Int, timeOut: LocalDateTime)

    @Transaction
    @Query("SELECT * FROM AttendanceEntity WHERE attendanceEmployeeId = :employeeId")
    suspend fun getAttendancesForEmployee(employeeId: Int): List<AttendanceEntity>


}