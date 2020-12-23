package dev.forcetower.playtime.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.WatchedItem
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class WatchedItemDao : BaseDao<WatchedItem>() {
    @Query("SELECT * FROM WatchedItem WHERE movieId = :movieId")
    protected abstract suspend fun getItemByIDDirect(movieId: Int): WatchedItem?

    override suspend fun getValueByIDDirect(value: WatchedItem): WatchedItem? {
        return getItemByIDDirect(value.movieId)
    }

    @Query("SELECT M.* FROM WatchedItem WLI INNER JOIN Movie M ON WLI.movieId = M.id ORDER BY WLI.addedAt")
    abstract fun getWatchedList(): PagingSource<Int, Movie>
}