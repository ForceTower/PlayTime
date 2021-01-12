package dev.forcetower.playtime.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.WatchlistItem
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WatchlistItemDao : BaseDao<WatchlistItem>() {
    @Query("SELECT * FROM WatchlistItem WHERE movieId = :movieId")
    protected abstract suspend fun getItemByIDDirect(movieId: Int): WatchlistItem?

    override suspend fun getValueByIDDirect(value: WatchlistItem): WatchlistItem? {
        return getItemByIDDirect(value.movieId)
    }

    @Query("SELECT M.* FROM WatchlistItem WLI INNER JOIN Movie M ON WLI.movieId = M.id ORDER BY WLI.addedAt")
    abstract fun getWatchList(): PagingSource<Int, Movie>

    @Query("SELECT COUNT(movieId) FROM WatchlistItem WHERE movieId = :movieId")
    abstract fun onWatchlist(movieId: Int): Flow<Boolean>

    @Query("SELECT COUNT(movieId) FROM WatchlistItem WHERE movieId = :movieId")
    protected abstract suspend fun onWatchlistDirect(movieId: Int): Boolean

    @Query("DELETE FROM WatchlistItem WHERE movieId = :movieId")
    abstract fun removeFromWatchList(movieId: Int)

    @Transaction
    open suspend fun toggle(movieId: Int) {
        if (onWatchlistDirect(movieId)) {
            removeFromWatchList(movieId)
        } else {
            insertIgnore(WatchlistItem(movieId))
        }
    }
}