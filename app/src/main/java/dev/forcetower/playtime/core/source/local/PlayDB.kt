package dev.forcetower.playtime.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.playtime.core.model.storage.*
import dev.forcetower.playtime.core.source.local.dao.*

@Database(entities = [
    Genre::class,
    Movie::class,
    MovieGenre::class,
    Video::class,
    Cast::class,
    Release::class,
    Image::class,
    MovieFeedIndex::class
], version = 1, exportSchema = true)
@TypeConverters(value = [DateConverters::class])
abstract class PlayDB : RoomDatabase() {
    abstract fun genres(): GenreDao
    abstract fun movies(): MovieDao
    abstract fun videos(): VideoDao
    abstract fun cast(): CastDao
    abstract fun releases(): ReleaseDao
    abstract val images: ImageDao
    abstract val feedIndex: FeedIndexDao
}