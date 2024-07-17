package me.sherief.attendease.data.mapper

import me.sherief.attendease.data.model.EmployeeEntity
import me.sherief.attendease.data.model.EmployeeWithAttendances
import me.sherief.attendease.domain.model.Employee
import javax.inject.Inject

class EmployeeMapper @Inject constructor() : DataMapper<EmployeeEntity, Employee> {
    override fun mapToDomain(cache: EmployeeEntity): Employee {
        return Employee(
            cache.employeeId.toString(),
            cache.firstName,
            cache.lastName,
            cache.mobile,
            cache.qrCode,
            emptyList()
        )
    }

    override fun mapFromDomain(domain: Employee): EmployeeEntity {
        return EmployeeEntity(
            domain.employeeId.toInt(),
            domain.firstName,
            domain.lastName,
            domain.mobile,
            domain.qrCode
        )
    }

    fun mapListFromDomain(domainList: List<Employee>): List<EmployeeEntity> {
        return domainList.map { mapFromDomain(it) }
    }

    fun mapListToDomain(cacheList: List<EmployeeEntity>): List<Employee> {
        return cacheList.map { mapToDomain(it) }
    }
}

class EmployeeWithAttendancesMapper @Inject constructor(
    private val employeeMapper: EmployeeMapper,
    private val attendanceMapper: AttendanceMapper
) :
    DataMapper<EmployeeWithAttendances, Employee> {
    override fun mapToDomain(cache: EmployeeWithAttendances): Employee {
        val attendances = cache.attendanceRecords.map { attendanceMapper.mapToDomain(it) }
        return Employee(
            cache.employee.employeeId.toString(),
            cache.employee.firstName,
            cache.employee.lastName,
            cache.employee.mobile,
            cache.employee.qrCode,
            attendances
        )
    }

    override fun mapFromDomain(domain: Employee): EmployeeWithAttendances {
        val employee = employeeMapper.mapFromDomain(domain)
        val attendances = domain.attendanceHistory?.map { attendanceMapper.mapFromDomain(it) }

        return EmployeeWithAttendances(
            employee,
            attendances ?: emptyList()
        )
    }

    fun mapListFromDomain(domainList: List<Employee>): List<EmployeeWithAttendances> {
        return domainList.map { mapFromDomain(it) }
    }

    fun mapListToDomain(cacheList: List<EmployeeWithAttendances>): List<Employee> {
        return cacheList.map { mapToDomain(it) }
    }

}