package me.sherief.attendease.domain.usecase

import kotlinx.datetime.LocalDate
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.map
import javax.inject.Inject

class GetEmployeeTotalHoursUseCase @Inject constructor(
    private val getRangeOfAttendancesByEmployeeIdUseCase: GetRangeOfAttendancesByEmployeeIdUseCase
) {
    suspend operator fun invoke(
        employeeId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<HoursAndMinutes> {

        return getRangeOfAttendancesByEmployeeIdUseCase(
            employeeId,
            startDate,
            endDate
        ).map {
            sumAttendanceHours(it)
        }

    }

    private fun sumAttendanceHours(attendanceList: List<Attendance>): HoursAndMinutes {
        val employeeTotalHours = HoursAndMinutes(0, 0)
        attendanceList.forEach {
            employeeTotalHours += it.totalHoursAndMinutes
        }
        return employeeTotalHours
    }
}