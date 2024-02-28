package me.sherief.attendease.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import me.sherief.attendease.domain.util.EmptyRecord
import me.sherief.attendease.domain.util.Result
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetEmployeeTotalHoursUseCaseTest {

    @Mock
    private lateinit var getRangeOfAttendancesByEmployeeIdUseCase: GetRangeOfAttendancesByEmployeeIdUseCase

    @InjectMocks
    private lateinit var getEmployeeTotalHoursUseCase: GetEmployeeTotalHoursUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun `should return Success with expectedTotalHours when given in range attendances`(): Unit =
        runBlocking {
            // Arrange
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 15)
            val endDate = LocalDate(2024, 2, 20)

            val attendances = listOf(
                Attendance(
                    "1",
                    "eid",
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0)
                ),
                Attendance(
                    "1",
                    "eid",
                    date = LocalDate(2024, 2, 20),
                    timeIn = LocalDateTime(2024, 2, 20, 8, 30, 0),
                    _timeOut = LocalDateTime(2024, 2, 20, 16, 15, 0)
                )
            )
            val expectedTotalHours = HoursAndMinutes(15, 45)

            `when`(getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate))
                .thenReturn(Result.success(attendances))

            // Act
            val result = getEmployeeTotalHoursUseCase.invoke(employeeId, startDate, endDate)

            // Assert
            assertThat(result.isSuccess).isTrue()
            assertThat(result).isEqualTo(Result.success(expectedTotalHours))
            verify(getRangeOfAttendancesByEmployeeIdUseCase, times(1)).invoke(
                employeeId,
                startDate,
                endDate
            )
        }

    @Test
    fun `should return EmptyRecord Error when filtered attendances for given out of range attendances`(): Unit =
        runBlocking {
            // Arrange
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 5)
            val endDate = LocalDate(2024, 2, 7)

            `when`(getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate))
                .thenReturn(
                    Result.failure(
                        EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")
                    )
                )

            // Act
            val result = getEmployeeTotalHoursUseCase.invoke(employeeId, startDate, endDate)

            // Assert
            assertThat(result.isFailure).isTrue()
            assertThat(result)
                .isEqualTo(Result.failure(EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")))
            verify(getRangeOfAttendancesByEmployeeIdUseCase, times(1)).invoke(
                employeeId,
                startDate,
                endDate
            )
        }

}