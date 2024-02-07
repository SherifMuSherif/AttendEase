package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.repository.EmployeeRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(employeeId: String): Result<Any> {
        return employeeRepository.deleteEmployee(employeeId)
    }
}