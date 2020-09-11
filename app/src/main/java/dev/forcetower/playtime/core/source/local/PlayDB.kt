package dev.forcetower.playtime.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.local.dao.GenreDao
import dev.forcetower.playtime.core.source.local.dao.MovieDao

@Database(entities = [
    Genre::class,
    Movie::class
], version = 2, exportSchema = true)
@TypeConverters(value = [DateConverters::class])
abstract class PlayDB : RoomDatabase() {
    abstract fun genres(): GenreDao
    abstract fun movies(): MovieDao
}