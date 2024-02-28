package me.sherief.attendease.domain.model

import com.google.common.truth.Truth
import org.junit.Test

class HoursAndMinutesTest {

    @Test
    fun `should return HoursAndMinutes(4, 30) when given 4 hours and 30 minutes as total`() {
        val list = listOf(
            HoursAndMinutes(1, 30),
            HoursAndMinutes(2, 30),
            HoursAndMinutes(0, 30)
        )
        val totalHoursAndMinutes = HoursAndMinutes(0, 0)
        list.forEach {
            totalHoursAndMinutes += it
        }
        Truth.assertThat(totalHoursAndMinutes).isEqualTo(HoursAndMinutes(4, 30))
    }

    @Test
    fun `should increase an hour and reset the minutes after 70 minutes given as total`() {
        val list = listOf(
            HoursAndMinutes(0, 13),
            HoursAndMinutes(0, 12),
            HoursAndMinutes(0, 45)
        )
        val totalHoursAndMinutes = HoursAndMinutes(0, 0)
        list.forEach {
            totalHoursAndMinutes += it
        }
        Truth.assertThat(totalHoursAndMinutes).isEqualTo(HoursAndMinutes(1, 10))

    }

    @Test
    fun `should increase an hour and reset the minutes after calling plusAssign operator with 70 minutes given in one object as total`() {
        val list = listOf(
            HoursAndMinutes(0, 70)
        )
        val totalHoursAndMinutes = HoursAndMinutes(0, 0)
        list.forEach {
            totalHoursAndMinutes += it
        }
        Truth.assertThat(totalHoursAndMinutes).isEqualTo(HoursAndMinutes(1, 10))

    }

    @Test
    fun `should increase an hour and reset the minutes when initialize a 70-minutes object`() {
        val hoursAndMinutes = HoursAndMinutes(0, 70)
        Truth.assertThat(hoursAndMinutes).isEqualTo(HoursAndMinutes(1, 10))
    }
}