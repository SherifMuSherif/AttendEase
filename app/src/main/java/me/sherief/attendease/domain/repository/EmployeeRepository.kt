package me.sherief.attendease.domain.repository

import me.sherief.attendease.domain.model.Employee
import me.sherief.attendease.domain.util.Result

interface EmployeeRepository {
    suspend fun getEmployeeById(id: String): Result<Employee>
    suspend fun getAllEmployees(): Result<List<Employee>>
    suspend fun addEmployee(employee: Employee): Result<Any>
    suspend fun generateQRCode(firstName: String, lastName: String, mobile: String): Result<String>
    suspend fun updateEmployee(employee: Employee): Result<Any>
    suspend fun deleteEmployee(id: String): Result<Any>
}