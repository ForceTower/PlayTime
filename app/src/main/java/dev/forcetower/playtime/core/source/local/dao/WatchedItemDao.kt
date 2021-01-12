package dev.forcetower.playtime.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.WatchedItem
import dev.forcetower.playtime.core.model.storage.WatchlistItem
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WatchedItemDao : BaseDao<WatchedItem>() {
    @Query("SELECT * FROM WatchedItem WHERE movieId = :movieId")
    protected abstract suspend fun getItemByIDDirect(movieId: Int): WatchedItem?

    override suspend fun getValueByIDDirect(value: WatchedItem): WatchedItem? {
        return getItemByIDDirect(value.movieId)
    }

    @Query("SELECT M.* FROM WatchedItem WLI INNER JOIN Movie M ON WLI.movieId = M.id ORDER BY WLI.addedAt")
    abstract fun getWatchedList(): PagingSource<Int, Movie>

    @Query("SELECT COUNT(movieId) FROM WatchedItem WHERE movieId = :movieId")
    abstract fun isWatched(movieId: Int): Flow<Boolean>

    @Query("SELECT COUNT(movieId) FROM WatchedItem WHERE movieId = :movieId")
    protected abstract suspend fun isWatchedDirect(movieId: Int): Boolean

    @Query("DELETE FROM WatchedItem WHERE movieId = :movieId")
    abstract fun removeFromWatched(movieId: Int)

    @Transaction
    open suspend fun toggle(movieId: Int) {
        if (isWatchedDirect(movieId)) {
            removeFromWatched(movieId)
        } else {
            insertIgnore(WatchedItem(movieId))
        }
    }
}