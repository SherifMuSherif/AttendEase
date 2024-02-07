package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.repository.EmployeeRepository
import me.sherief.attendease.domain.util.Result
import javax.inject.Inject

class GenerateQRCodeUseCase @Inject constructor(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        mobile: String
    ): Result<String> {
        return employeeRepository.generateQRCode(firstName, lastName, mobile)
    }
}