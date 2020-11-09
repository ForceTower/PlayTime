package dev.forcetower.playtime.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.playtime.core.model.storage.*
import dev.forcetower.playtime.core.source.local.dao.CastDao
import dev.forcetower.playtime.core.source.local.dao.GenreDao
import dev.forcetower.playtime.core.source.local.dao.MovieDao
import dev.forcetower.playtime.core.source.local.dao.VideoDao

@Database(entities = [
    Genre::class,
    Movie::class,
    MovieGenre::class,
    Video::class,
    Cast::class
], version = 1, exportSchema = true)
@TypeConverters(value = [DateConverters::class])
abstract class PlayDB : RoomDatabase() {
    abstract fun genres(): GenreDao
    abstract fun movies(): MovieDao
    abstract fun videos(): VideoDao
    abstract fun cast(): CastDao
}