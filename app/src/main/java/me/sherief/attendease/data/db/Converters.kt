package me.sherief.attendease.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.sherief.attendease.domain.util.toEpochMilliseconds
import me.sherief.attendease.domain.util.toLocalDateTimeWithZone


class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? = value?.toLocalDateTimeWithZone()

    @TypeConverter
    fun toTimestamp(datetime: LocalDateTime?): Long? = datetime?.toEpochMilliseconds()
}

class LocalDateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? = value?.toLocalDateTimeWithZone()?.date

    @TypeConverter
    fun toTimestamp(date: LocalDate?): Long? = date?.toEpochMilliseconds()
}
