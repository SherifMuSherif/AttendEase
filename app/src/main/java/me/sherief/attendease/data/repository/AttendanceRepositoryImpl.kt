package me.sherief.attendease.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.data.db.AppDatabase
import me.sherief.attendease.data.mapper.AttendanceMapper
import me.sherief.attendease.data.util.safeResult
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.ErrorHandler
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.map
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val attendanceMapper: AttendanceMapper,
    private val errorHandler: ErrorHandler
) : AttendanceRepository {
    override suspend fun getAttendanceById(id: String): Result<Attendance> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.attendanceDao().getAttendanceById(id.toInt())
            }.map {
                attendanceMapper.mapToDomain(it)
            }
        }
    }

    override suspend fun getAllAttendances(): Result<List<Attendance>> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.attendanceDao().getAllAttendances()
            }.map { listAttendancesEntity ->
                listAttendancesEntity.map { attendanceEntity ->
                    attendanceMapper.mapToDomain(attendanceEntity)
                }
            }
        }
    }

    override suspend fun getAttendancesByEmployeeId(employeeId: String): Result<List<Attendance>> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.attendanceDao().getAttendancesForEmployee(employeeId.toInt())
            }.map { attendanceEntities ->
                attendanceEntities.map { attendanceMapper.mapToDomain(it) }
            }
        }
    }

    override suspend fun addAttendance(attendance: Attendance): Result<Attendance> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.attendanceDao()
                    .insertAttendance(attendanceMapper.mapFromDomain(attendance))
            }.map {
                attendance
            }
        }
    }

    override suspend fun updateAttendanceTimeOut(
        attendanceId: String,
        timeOut: LocalDateTime
    ): Result<Attendance> {
        lateinit var attendance: Attendance
        return withContext(Dispatchers.IO) {

            getAttendanceById(attendanceId).map { attendance = it }

            safeResult(errorHandler) {
                val newAttendance = attendance.copy(_timeOut = timeOut)
                appDatabase.attendanceDao()
                    .updateAttendance(attendanceMapper.mapFromDomain(newAttendance))

                newAttendance
            }
        }
    }
}