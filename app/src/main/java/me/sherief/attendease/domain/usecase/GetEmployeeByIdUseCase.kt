package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Employee
import me.sherief.attendease.domain.repository.EmployeeRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject

class GetEmployeeByIdUseCase @Inject constructor(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(employeeId: String): Result<Employee> {
        return employeeRepository.getEmployeeById(employeeId)
    }
}