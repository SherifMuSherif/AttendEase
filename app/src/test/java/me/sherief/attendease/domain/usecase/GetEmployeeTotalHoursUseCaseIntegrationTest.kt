package me.sherief.attendease.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.EmptyRecord
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.exceptionOrNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetEmployeeTotalHoursUseCaseIntegrationTest {

    @Mock
    private lateinit var attendanceRepository: AttendanceRepository

    private lateinit var getRangeOfAttendancesByEmployeeIdUseCase: GetRangeOfAttendancesByEmployeeIdUseCase

    private lateinit var getAttendancesByEmployeeIdUseCase: GetAttendancesByEmployeeIdUseCase

    private lateinit var getEmployeeTotalHoursUseCase: GetEmployeeTotalHoursUseCase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getAttendancesByEmployeeIdUseCase =
            Mockito.spy(GetAttendancesByEmployeeIdUseCase(attendanceRepository))

        getRangeOfAttendancesByEmployeeIdUseCase =
            Mockito.spy(GetRangeOfAttendancesByEmployeeIdUseCase(getAttendancesByEmployeeIdUseCase))

        getEmployeeTotalHoursUseCase =
            GetEmployeeTotalHoursUseCase(getRangeOfAttendancesByEmployeeIdUseCase)

    }

    @Test
    fun `should return total hours when attendances found in range`(): Unit = runBlocking {
        val employeeId = "testEmployeeId"
        val startDate = LocalDate(2024, 2, 5)
        val endDate = LocalDate(2024, 2, 7)

        val attendances = listOf(
            Attendance(
                "id0",
                employeeId,
                date = LocalDate(2024, 2, 5),
                timeIn = LocalDateTime(2024, 2, 5, 9, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 5, 17, 0, 0),
            ),
            Attendance(
                "id1",
                employeeId,
                date = LocalDate(2024, 2, 6),
                timeIn = LocalDateTime(2024, 2, 6, 8, 30, 0),
                _timeOut = LocalDateTime(2024, 2, 6, 16, 15, 0),
            ),
            Attendance(
                "id2",
                employeeId,
                date = LocalDate(2024, 2, 8),
                timeIn = LocalDateTime(2024, 2, 8, 9, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 8, 17, 0, 0),
            )
        )

        val expectedTotalHours = HoursAndMinutes(16, 45)

        `when`(getAttendancesByEmployeeIdUseCase(employeeId))
            .thenReturn(Result.success(attendances))

        val result = getEmployeeTotalHoursUseCase
            .invoke(employeeId, startDate, endDate)

        assertThat(result.isSuccess).isTrue()
        assertThat(result).isEqualTo(Result.success(expectedTotalHours))

        verify(attendanceRepository, times(1)).getAttendancesByEmployeeId(employeeId)
        verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)
        verify(getRangeOfAttendancesByEmployeeIdUseCase, times(1))
            .invoke(employeeId, startDate, endDate)
    }

    @Test
    fun `should return empty record error when no attendances found in range`(): Unit =
        runBlocking {
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 5)
            val endDate = LocalDate(2024, 2, 7)

            val attendances = listOf(
                Attendance(
                    "id0",
                    employeeId,
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0),
                ),
                Attendance(
                    "id1",
                    employeeId,
                    date = LocalDate(2024, 2, 16),
                    timeIn = LocalDateTime(2024, 2, 16, 8, 30, 0),
                    _timeOut = LocalDateTime(2024, 2, 16, 16, 15, 0),
                ),
                Attendance(
                    "id2",
                    employeeId,
                    date = LocalDate(2024, 2, 18),
                    timeIn = LocalDateTime(2024, 2, 18, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 18, 17, 0, 0),
                )
            )

            `when`(getAttendancesByEmployeeIdUseCase(employeeId))
                .thenReturn(Result.success(attendances))

            val result = getEmployeeTotalHoursUseCase
                .invoke(employeeId, startDate, endDate)

            assertThat(result.isFailure).isTrue()
            assertThat(result).isEqualTo(Result.failure(EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")))

            verify(attendanceRepository, times(1)).getAttendancesByEmployeeId(employeeId)
            verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)
            verify(getRangeOfAttendancesByEmployeeIdUseCase, times(1))
                .invoke(employeeId, startDate, endDate)
        }

    @Test
    fun `should return empty record error when getAttendancesByEmployeeIdUseCase return emptyList`(): Unit =
        runBlocking {
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 15)
            val endDate = LocalDate(2024, 2, 17)

            val emptyAttendances = emptyList<Attendance>()

            `when`(getAttendancesByEmployeeIdUseCase(employeeId))
                .thenReturn(Result.success(emptyAttendances))

            val result = GetEmployeeTotalHoursUseCase(getRangeOfAttendancesByEmployeeIdUseCase)
                .invoke(employeeId, startDate, endDate)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull())
                .isInstanceOf(EmptyRecord::class.java)
            assertThat((result.exceptionOrNull() as EmptyRecord).message)
                .isEqualTo("No Attendances found for this EmployeeId $employeeId form $startDate to $endDate")

            verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)
            verify(getRangeOfAttendancesByEmployeeIdUseCase, times(1))
                .invoke(employeeId, startDate, endDate)
        }
}
