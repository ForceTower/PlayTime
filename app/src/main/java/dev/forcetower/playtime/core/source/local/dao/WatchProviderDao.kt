package dev.forcetower.playtime.core.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.MovieWatchProvider
import dev.forcetower.playtime.core.model.storage.WatchProvider
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WatchProviderDao : BaseDao<WatchProvider>() {
    @Query("SELECT * FROM WatchProvider WHERE id = :id")
    abstract suspend fun getByIdDirect(id: Int): WatchProvider?

    override suspend fun getValueByIDDirect(value: WatchProvider): WatchProvider? {
        return getByIdDirect(value.id)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAssociations(associations: List<MovieWatchProvider>)

    @Query("DELETE FROM MovieWatchProvider WHERE movieId = :movieId")
    abstract suspend fun deleteAssociationsFromMovie(movieId: Int)

    @Query("SELECT DISTINCT WP.* FROM WatchProvider WP INNER JOIN MovieWatchProvider MWP ON MWP.providerId = WP.id WHERE MWP.movieId = :movieId")
    abstract fun getProvidersOf(movieId: Int): Flow<List<WatchProvider>>
}
