package dev.forcetower.playtime.core.source.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.playtime.core.model.storage.Cast
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieFeedIndex
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.MovieRecommendation
import dev.forcetower.playtime.core.model.storage.MovieReleaseFeedIndex
import dev.forcetower.playtime.core.model.storage.MovieWatchProvider
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.playtime.core.model.storage.WatchProvider
import dev.forcetower.playtime.core.model.storage.WatchedItem
import dev.forcetower.playtime.core.model.storage.WatchlistItem
import dev.forcetower.playtime.core.source.local.dao.CastDao
import dev.forcetower.playtime.core.source.local.dao.FeedIndexDao
import dev.forcetower.playtime.core.source.local.dao.GenreDao
import dev.forcetower.playtime.core.source.local.dao.ImageDao
import dev.forcetower.playtime.core.source.local.dao.MovieDao
import dev.forcetower.playtime.core.source.local.dao.MovieRecommendationDao
import dev.forcetower.playtime.core.source.local.dao.ReleaseDao
import dev.forcetower.playtime.core.source.local.dao.ReleaseIndexDao
import dev.forcetower.playtime.core.source.local.dao.VideoDao
import dev.forcetower.playtime.core.source.local.dao.WatchProviderDao
import dev.forcetower.playtime.core.source.local.dao.WatchedItemDao
import dev.forcetower.playtime.core.source.local.dao.WatchlistItemDao

@Database(
    entities = [
        Genre::class,
        Movie::class,
        MovieGenre::class,
        Video::class,
        Cast::class,
        Release::class,
        Image::class,
        MovieFeedIndex::class,
        WatchedItem::class,
        WatchlistItem::class,
        MovieReleaseFeedIndex::class,
        WatchProvider::class,
        MovieWatchProvider::class,
        MovieRecommendation::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(value = [DateConverters::class])
abstract class PlayDB : RoomDatabase() {
    abstract fun genres(): GenreDao
    abstract fun movies(): MovieDao
    abstract fun videos(): VideoDao
    abstract fun cast(): CastDao
    abstract fun releases(): ReleaseDao
    abstract val images: ImageDao
    abstract val feedIndex: FeedIndexDao
    abstract val releaseFeedIndex: ReleaseIndexDao
    abstract val watchlist: WatchlistItemDao
    abstract val watched: WatchedItemDao
    abstract val providers: WatchProviderDao
    abstract val recommendations: MovieRecommendationDao
}
