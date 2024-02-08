package me.sherief.attendease.domain.usecase

import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.util.Result
import java.util.Date
import javax.inject.Inject

class GetRangeOfAttendancesByEmployeeIdUseCase @Inject constructor(
    private val getAttendancesByEmployeeIdUseCase: GetAttendancesByEmployeeIdUseCase
) {
    suspend operator fun invoke(
        employeeId: String,
        startDate: Date,
        endDate: Date
    ): Result<List<Attendance>> {
        val attendancesResult = getAttendancesByEmployeeIdUseCase(employeeId)

        return if (attendancesResult is Result.Success) {
            val filteredAttendances = attendancesResult.data.filter {
                (it.date.equals(startDate) || it.date.after(startDate)) && (it.date.equals(endDate) || it.date.before(
                    endDate
                ))
            }
            Result.Success(filteredAttendances)
        } else {
            attendancesResult
        }
    }
}