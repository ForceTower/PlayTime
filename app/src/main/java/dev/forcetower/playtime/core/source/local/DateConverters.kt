package dev.forcetower.playtime.core.source.local

import androidx.room.TypeConverter
import java.time.LocalDate

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
}