package me.sherief.attendease.data.mapper

import me.sherief.attendease.data.model.AttendanceEntity
import me.sherief.attendease.domain.model.Attendance
import me.sherief.attendease.domain.model.HoursAndMinutes
import javax.inject.Inject

class AttendanceMapper @Inject constructor() : DataMapper<AttendanceEntity, Attendance> {
    override fun mapToDomain(cache: AttendanceEntity): Attendance {
        return Attendance(
            cache.attendanceId.toString(),
            cache.attendanceEmployeeId.toString(),
            cache.date,
            cache.timeIn,
            cache.timeOut,
            HoursAndMinutes(
                cache.totalHours,
                cache.totalMinutes
            )
        )
    }

    override fun mapFromDomain(domain: Attendance): AttendanceEntity {
        return AttendanceEntity(
            domain.attendanceId.toInt(),
            domain.employeeId.toInt(),
            domain.date,
            domain.timeIn,
            domain.timeOut,
            domain.totalHoursAndMinutes.hours,
            domain.totalHoursAndMinutes.minutes
        )
    }

    fun mapListFromDomain(domainList: List<Attendance>): List<AttendanceEntity> {
        return domainList.map { mapFromDomain(it) }
    }

    fun mapListToDomain(cacheList: List<AttendanceEntity>): List<Attendance> {
        return cacheList.map { mapToDomain(it) }
    }
}

class HoursAndMinutesMapper @Inject constructor() : DataMapper<Pair<Long, Int>, HoursAndMinutes> {
    override fun mapToDomain(cache: Pair<Long, Int>): HoursAndMinutes {
        return HoursAndMinutes(cache.first, cache.second)
    }

    override fun mapFromDomain(domain: HoursAndMinutes): Pair<Long, Int> {
        return Pair(domain.hours, domain.minutes)
    }

}