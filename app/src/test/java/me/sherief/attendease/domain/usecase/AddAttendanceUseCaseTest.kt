package me.sherief.attendease.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import me.sherief.attendease.domain.repository.AttendanceRepository
import me.sherief.attendease.domain.util.InvalidTimeRangeException
import me.sherief.attendease.domain.util.Result
import me.sherief.attendease.domain.util.doTry
import me.sherief.attendease.domain.util.fold
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AddAttendanceUseCaseTest {
    @Mock
    private lateinit var attendanceRepository: AttendanceRepository

    @InjectMocks
    private lateinit var addAttendanceUseCase: AddAttendanceUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should throw InvalidTimeRangeException when initialize Attendance with given timeOut not after timeIn`() {
        // timeOut before timeIn
        assertThrows(InvalidTimeRangeException::class.java) {
            val attendance = Attendance(
                attendanceId = "id0",
                employeeId = "testEmployeeId",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 20, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 15, 13, 0, 0),
                _totalHoursAndMinutes = HoursAndMinutes(0, 0)
            )
            runBlocking {
                addAttendanceUseCase.invoke(attendance)
            }
        }
        // timeOut equal timeIn
        assertThrows(InvalidTimeRangeException::class.java) {
            val attendance = Attendance(
                attendanceId = "id0",
                employeeId = "testEmployeeId",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 20, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 15, 20, 0, 0),
                _totalHoursAndMinutes = HoursAndMinutes(0, 0)
            )
            runBlocking {
                addAttendanceUseCase.invoke(attendance)
            }
        }
    }

    @Test
    fun `should return Failure with InvalidTimeRangeException when initialize Attendance in doTry function with given timeOut not after timeIn`() =
        runBlocking {
            // Arrange
            // timeOut equal timeIn

            val attendance = doTry {
                Attendance(
                    attendanceId = "id0",
                    employeeId = "testEmployeeId",
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 20, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 20, 0, 0),
                    _totalHoursAndMinutes = HoursAndMinutes(0, 0)
                )
            }.fold(
                onFailure = {
                    Result.failure(it)
                }, onSuccess = {
                    // Act
                    addAttendanceUseCase.invoke(it)
                }
            )

            // timeOut before timeIn
            val attendance2 = doTry {
                Attendance(
                    attendanceId = "id0",
                    employeeId = "testEmployeeId",
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 20, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 13, 0, 0),
                    _totalHoursAndMinutes = HoursAndMinutes(0, 0)
                )
            }.fold(
                onFailure = {
                    Result.failure(it)
                }, onSuccess = {
                    // Act
                    addAttendanceUseCase.invoke(it)
                }
            )

            // Assert
            assertThat(attendance).isEqualTo(Result.failure(InvalidTimeRangeException("timeOut must be after timeIn.")))
            assertThat(attendance2).isEqualTo(Result.failure(InvalidTimeRangeException("timeOut must be after timeIn.")))


        }

    @Test
    fun `should return Failure with InvalidTimeRangeException when initialize Attendance in invoke operator taking lambda given timeOut before timeIn`() =
        runBlocking {
            // Arrange
            // Act
            val result = addAttendanceUseCase.invoke {
                Attendance(
                    attendanceId = "id0",
                    employeeId = "testEmployeeId",
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 20, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 13, 0, 0),
                    _totalHoursAndMinutes = HoursAndMinutes(0, 0)
                )
            }

            // Assert
            assertThat(result).isEqualTo(Result.failure(InvalidTimeRangeException("timeOut must be after timeIn.")))
        }

    @Test
    fun `should return attendance with 8 totalHoursAndMinutes when call normal invoke operator with given 8 hour difference LocalDateTimes`(): Unit =
        runBlocking {
            // Arrange
            val attendance = Attendance(
                attendanceId = "id0",
                employeeId = "testEmployeeId",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0)
            )
            val expectedTotalHours = HoursAndMinutes(8, 0)

            Mockito.`when`(attendanceRepository.addAttendance(attendance))
                .thenReturn(Result.success(attendance.copy(_totalHoursAndMinutes = expectedTotalHours)))


            // Act
            val actualResult = addAttendanceUseCase.invoke(attendance)

            // Assert
            assertThat(actualResult).isEqualTo(Result.success(attendance.copy(_totalHoursAndMinutes = expectedTotalHours)))
            verify(attendanceRepository, times(1)).addAttendance(attendance)
        }

    @Test
    fun `should return attendance with 8 totalHoursAndMinutes when call invoke operator taking lambda with given 8 hour difference LocalDateTimes`(): Unit =
        runBlocking {
            // Arrange
            val expectedAttendance = Attendance(
                attendanceId = "id0",
                employeeId = "testEmployeeId",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0),
                _totalHoursAndMinutes = HoursAndMinutes(8, 0)
            )

            Mockito.`when`(attendanceRepository.addAttendance(expectedAttendance))
                .thenReturn(Result.success(expectedAttendance))


            // Act
            val actualResult = addAttendanceUseCase.invoke {
                Attendance(
                    attendanceId = "id0",
                    employeeId = "testEmployeeId",
                    date = LocalDate(2024, 2, 15),
                    timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                    _timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0)
                )
            }

            // Assert
            assertThat(actualResult).isEqualTo(Result.success(expectedAttendance))
            verify(attendanceRepository, times(1)).addAttendance(expectedAttendance)
        }

}