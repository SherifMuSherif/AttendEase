package me.sherief.attendease.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import me.sherief.attendease.domain.util.EmptyRecord
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.Result.Failure
import me.sherief.attendease.domain.util.Result.Success
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetRangeOfAttendancesByEmployeeIdUseCaseTest {

    @Mock
    private lateinit var getAttendancesByEmployeeIdUseCase: GetAttendancesByEmployeeIdUseCase

    @InjectMocks
    private lateinit var getRangeOfAttendancesByEmployeeIdUseCase: GetRangeOfAttendancesByEmployeeIdUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should return EmptyRecord Failure when given out of range attendances for specific employeeId`(): Unit =
        runBlocking {
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 5)
            val endDate = LocalDate(2024, 2, 7)

            val attendances = Result.success(
                listOf(
                    Attendance(
                        "id0",
                        employeeId,
                        date = LocalDate(2024, 2, 15),
                        timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                        _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0)
                    ), Attendance(
                        "id1",
                        employeeId,
                        date = LocalDate(2024, 2, 15),
                        timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                        _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0)
                    )
                )
            )

            `when`(getAttendancesByEmployeeIdUseCase(employeeId))
                .thenReturn(attendances)

            val result = getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate)

            assertThat(result.isFailure).isTrue()
            assertThat(result)
                .isEqualTo(Result.failure(EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")))
            verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)
        }

    @Test
    fun `should return EmptyRecord Failure when given wrong employeeId`(): Unit = runBlocking {
        val employeeId = "testEmployeeId"
        val wrongEmployeeId = "wrongEmployeeId"

        val startDate = LocalDate(2024, 2, 5)
        val endDate = LocalDate(2024, 2, 7)

        val attendances = listOf(
            Attendance(
                "id0",
                employeeId,
                date = LocalDate(2024, 2, 5),
                timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0),
                _totalHoursAndMinutes = HoursAndMinutes(0, 0)
            )
        )

        `when`(getAttendancesByEmployeeIdUseCase.invoke(wrongEmployeeId)).thenReturn(
            Failure(
                EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")
            )
        )

        val result = getRangeOfAttendancesByEmployeeIdUseCase(wrongEmployeeId, startDate, endDate)

        assertThat(result.isFailure).isTrue()
        assertThat(result)
            .isEqualTo(Result.failure(EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")))
        verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(wrongEmployeeId)
    }

    @Test
    fun `should return success filtered attendances {0,1} when given right employeeId and date range`(): Unit =
        runBlocking {
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 5)
            val endDate = LocalDate(2024, 2, 6)

            val attendances = listOf(
                Attendance(
                    "id0",
                    employeeId,
                    date = LocalDate(2024, 2, 5),
                    timeIn = LocalDateTime(2024, 2, 5, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 5, 17, 0, 0)
                ), Attendance(
                    "id1",
                    employeeId,
                    date = LocalDate(2024, 2, 6),
                    timeIn = LocalDateTime(2024, 2, 6, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 6, 17, 0, 0)
                ), Attendance(
                    "id2",
                    employeeId,
                    date = LocalDate(2024, 2, 7),
                    timeIn = LocalDateTime(2024, 2, 7, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 7, 17, 0, 0)
                ), Attendance(
                    "id2",
                    employeeId,
                    date = LocalDate(2024, 2, 8),
                    timeIn = LocalDateTime(2024, 2, 8, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 8, 17, 0, 0)
                )
            )

            `when`(getAttendancesByEmployeeIdUseCase.invoke(employeeId)).thenReturn(
                Success(
                    attendances
                )
            )

            val result = getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate)

            assertThat(result.isSuccess).isTrue()
            assertThat(result).isEqualTo(Result.success(attendances.slice(IntRange(0, 1))))
            verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)
        }


    @Test
    fun `should return EmptyRecord Failure when given emptyList`(): Unit =
        runBlocking {
            val employeeId = "testEmployeeId"
            val startDate = LocalDate(2024, 2, 5)
            val endDate = LocalDate(2024, 2, 6)

            `when`(getAttendancesByEmployeeIdUseCase.invoke(employeeId)).thenReturn(
                Success(
                    emptyList<Attendance>()
                )
            )

            val result = getRangeOfAttendancesByEmployeeIdUseCase(employeeId, startDate, endDate)

            assertThat(result.isFailure).isTrue()
            assertThat(result)
                .isEqualTo(Result.failure(EmptyRecord("No Attendances found for this EmployeeId testEmployeeId form $startDate to $endDate")))
            verify(getAttendancesByEmployeeIdUseCase, times(1)).invoke(employeeId)

        }


}