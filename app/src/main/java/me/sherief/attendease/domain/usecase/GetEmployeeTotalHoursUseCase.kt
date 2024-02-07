package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.util.Result
import java.util.Date
import javax.inject.Inject

class GetEmployeeTotalHoursUseCase @Inject constructor(
    private val getRangeOfAttendancesByEmployeeIdUseCase: GetRangeOfAttendancesByEmployeeIdUseCase
) {
    suspend operator fun invoke(
        employeeId: String,
        startDate: Date,
        endDate: Date
    ): Float {
        val attendancesResult =
            getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate)
        var employeeTotalHours = 0f
        when (attendancesResult) {
            is Result.Success -> {
                attendancesResult.data.forEach {
                    employeeTotalHours += it.totalHours
                }
            }

            is Result.Error -> TODO()
        }

        return employeeTotalHours
    }
}