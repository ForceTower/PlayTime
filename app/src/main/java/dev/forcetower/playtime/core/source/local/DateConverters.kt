package dev.forcetower.playtime.core.source.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

object DateConverters {
    @TypeConverter
    fun localDateToLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun longToLocalDate(long: Long?): LocalDate? {
        long ?: return null
        return LocalDate.ofEpochDay(long)
    }

    @JvmStatic
    @TypeConverter
    fun localDateTimeTimeToLong(date: LocalDateTime?): Long? {
        return date?.toInstant(ZoneOffset.UTC)?.epochSecond
    }

    @TypeConverter
    @JvmStatic
    fun longToLocalDateTime(value: Long?): LocalDateTime? {
        value ?: return null
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.of("UTC"))
    }

    @JvmStatic
    @TypeConverter
    fun zonedDateTimeToLong(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.epochSecond
    }

    @TypeConverter
    @JvmStatic
    fun longToZonedDateTime(value: Long?): ZonedDateTime? {
        value ?: return null
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.of("UTC"))
    }
}
