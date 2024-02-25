package me.sherief.attendease.domain.model

import com.google.common.truth.Truth.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.util.InvalidTimeRangeException
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AttendanceTest {

    @Test
    fun `should return exact totalHoursAndMinutes when given Attendance with initialized HoursAndMinutes`() {
        val attendance = Attendance(
            attendanceId = "1",
            employeeId = "1",
            date = LocalDate(2024, 2, 15),
            timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
            timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0),
            _totalHoursAndMinutes = HoursAndMinutes(1, 0)
        )

        assertThat(attendance.totalHoursAndMinutes).isEqualTo(HoursAndMinutes(1, 0))
    }

    @Test
    fun `should return HoursAndMinutes(8, 0) when given 8 hours difference LocalDateTimes`() {
        val attendance = Attendance(
            attendanceId = "1",
            employeeId = "1",
            date = LocalDate(2024, 2, 15),
            timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
            timeOut = LocalDateTime(2024, 2, 15, 17, 0, 0),
        )

        // Get the private method by its name
        val method = attendance.javaClass.getDeclaredMethod("calculateTotalHours")
        // Make the method accessible
        method.isAccessible = true
        // Call the calculateTotalHours method and store the result
        val result = method.invoke(attendance) as HoursAndMinutes

        // Assert that the result is equal to the mock object
        assertThat(result).isEqualTo(HoursAndMinutes(8, 0))
        assertThat(attendance.totalHoursAndMinutes).isEqualTo(HoursAndMinutes(8, 0))
    }

    @Test
    fun `should return HoursAndMinutes(1, 30) when given 1 hour and 30 minutes difference LocalDateTimes`() {
        val attendance = Attendance(
            attendanceId = "1",
            employeeId = "1",
            date = LocalDate(2024, 2, 15),
            timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
            timeOut = LocalDateTime(2024, 2, 15, 10, 30, 0),
            _totalHoursAndMinutes = HoursAndMinutes(0, 0)
        )

        assertThat(attendance.totalHoursAndMinutes).isEqualTo(HoursAndMinutes(1, 30))
    }

    @Test
    fun `should throw InvalidTimeRangeException when given timeOut before timeIn`() {
        // Assert that an exception is thrown when timeOut is before timeIn
        assertThrows(InvalidTimeRangeException::class.java) {
            Attendance(
                attendanceId = "1",
                employeeId = "1",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 17, 0, 0),
                timeOut = LocalDateTime(2024, 2, 15, 9, 0, 0),
            )
        }
    }

    @Test
    fun `should throw InvalidTimeRangeException when given timeOut is equal timeIn`() {
        // Assert that an exception is thrown when timeOut is equal timeIn
        assertThrows(InvalidTimeRangeException::class.java) {
            Attendance(
                attendanceId = "1",
                employeeId = "1",
                date = LocalDate(2024, 2, 15),
                timeIn = LocalDateTime(2024, 2, 15, 9, 0, 0),
                timeOut = LocalDateTime(2024, 2, 15, 9, 0, 0),
            )
        }
    }
}



