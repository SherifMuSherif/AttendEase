package me.sherief.attendease.domain.usecase

import kotlinx.datetime.LocalDate
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.util.EmptyRecord
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.flatMap
import me.sherief.attendease.domain.util.isDateInRange
import javax.inject.Inject

class GetRangeOfAttendancesByEmployeeIdUseCase @Inject constructor(
    private val getAttendancesByEmployeeIdUseCase: GetAttendancesByEmployeeIdUseCase
) {
    suspend operator fun invoke(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<Attendance>> {


        return getAttendancesByEmployeeIdUseCase(employeeId)
            .flatMap { attendancesResult ->
                attendancesResult.filter { attendance ->
                    isDateInRange(attendance.date, startDate, endDate)
                }.takeIf { it.isNotEmpty() }
                    ?.let { Result.success(it) }
                    ?: Result.failure(EmptyRecord("No Attendances found for this EmployeeId $employeeId form $startDate to $endDate"))
            }
    }

}