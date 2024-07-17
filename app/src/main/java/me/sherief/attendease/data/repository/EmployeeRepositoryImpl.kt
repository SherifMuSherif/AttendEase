package me.sherief.attendease.data.repository

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sherief.attendease.data.db.AppDatabase
import me.sherief.attendease.data.mapper.EmployeeMapper
import me.sherief.attendease.data.mapper.EmployeeWithAttendancesMapper
import me.sherief.attendease.data.util.safeResult
import me.sherief.attendease.domain.model.Employee
import me.sherief.attendease.domain.repository.EmployeeRepository
import me.sherief.attendease.domain.util.ErrorHandler
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.map
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val employeeMapper: EmployeeMapper,
    private val employeeWithAttendancesMapper: EmployeeWithAttendancesMapper,
    private val errorHandler: ErrorHandler
) : EmployeeRepository {
    override suspend fun getEmployeeById(id: String): Result<Employee> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.employeeDao().getEmployeeByIdWithAttendances(id.toInt())
            }.map {
                employeeWithAttendancesMapper.mapToDomain(it)
            }
        }
    }

    override suspend fun getAllEmployees(): Result<List<Employee>> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.employeeDao().getAllEmployeesWithAttendances()
            }.map { listEmployeeEntity ->
                listEmployeeEntity.map { employeeEntity ->
                    employeeWithAttendancesMapper.mapToDomain(employeeEntity)
                }
            }
        }
    }

    override suspend fun addEmployee(employee: Employee): Result<Any> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.employeeDao()
                    .insertEmployee(employeeMapper.mapFromDomain(employee))
            }.map {
                employee
            }
        }
    }

    override suspend fun generateQRCode(
        firstName: String,
        lastName: String,
        mobile: String
    ): Result<String> {

        val string = buildString {
            append(firstName.trim())
            append(lastName.trim())
            append(mobile.trim())
        }.run { Base64.encodeToString(toByteArray(), Base64.NO_WRAP) }

        return Result.success(string)
    }

    override suspend fun updateEmployee(employee: Employee): Result<Any> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                appDatabase.employeeDao()
                    .updateEmployee(employeeMapper.mapFromDomain(employee))
            }.map {
                employee
            }
        }
    }

    override suspend fun deleteEmployee(id: String): Result<Any> {
        return withContext(Dispatchers.IO) {
            safeResult(errorHandler) {
                getEmployeeById(id).map {
                    appDatabase.employeeDao()
                        .deleteEmployee(employeeMapper.mapFromDomain(it))
                }
            }.map {
                id
            }
        }
    }
}