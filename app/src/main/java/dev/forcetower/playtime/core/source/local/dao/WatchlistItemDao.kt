package dev.forcetower.playtime.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.playtime.core.model.internal.MovieWithReleases
import dev.forcetower.playtime.core.model.internal.MovieWithWatchlist
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.WatchlistItem
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
abstract class WatchlistItemDao : BaseDao<WatchlistItem>() {
    @Query("SELECT M.* FROM WatchlistItem WLI INNER JOIN Movie M ON WLI.movieId = M.id WHERE M.releaseDate > :today ORDER BY M.releaseDate")
    abstract fun getWatchlistUnreleased(today: LocalDate = LocalDate.now()): PagingSource<Int, Movie>

    @Query("SELECT COUNT(M.id) FROM WatchlistItem WLI INNER JOIN Movie M ON WLI.movieId = M.id WHERE M.releaseDate > :today")
    abstract fun getCountWatchlistUnreleased(today: LocalDate = LocalDate.now()): Flow<Int>

    @Query("SELECT M.* FROM WatchlistItem WLI INNER JOIN Movie M ON WLI.movieId = M.id WHERE M.releaseDate <= :today ORDER BY WLI.addedAt")
    abstract fun getWatchListReleased(today: LocalDate = LocalDate.now()): PagingSource<Int, Movie>

    @Query("SELECT COUNT(M.id) FROM WatchlistItem WLI INNER JOIN Movie M ON WLI.movieId = M.id WHERE M.releaseDate <= :today")
    abstract fun getCountWatchListReleased(today: LocalDate = LocalDate.now()): Flow<Int>

    @Query("SELECT COUNT(movieId) FROM WatchlistItem WHERE movieId = :movieId")
    abstract fun onWatchlist(movieId: Int): Flow<Boolean>

    @Query("SELECT COUNT(movieId) FROM WatchlistItem WHERE movieId = :movieId")
    protected abstract suspend fun onWatchlistDirect(movieId: Int): Boolean

    @Query("DELETE FROM WatchlistItem WHERE movieId = :movieId")
    abstract fun removeFromWatchList(movieId: Int)

    @Query("SELECT M.id FROM Movie M INNER JOIN WatchlistItem WI ON WI.movieId = M.id WHERE WI.notifiedAt IS NULL")
    abstract suspend fun getPendingMovieIdNotifications(): List<Int>

    @Transaction
    @Query("SELECT M.* FROM Movie M INNER JOIN WatchlistItem WI ON WI.movieId = M.id WHERE WI.notifiedAt IS NULL")
    abstract suspend fun getPendingMovieNotifications(): List<MovieWithReleases>

    @Transaction
    @Query("SELECT M.* FROM Movie M INNER JOIN WatchlistItem WI ON WI.movieId = M.id WHERE WI.notifiedAt IS NULL AND WI.targetDate IS NOT NULL AND WI.targetDate <= :date")
    abstract suspend fun getCurrentPendingNotifications(date: LocalDate): List<MovieWithWatchlist>

    @Query("UPDATE WatchlistItem SET targetDate = :date WHERE movieId = :movieId")
    abstract suspend fun updateTargetDate(movieId: Int, date: LocalDate)

    @Query("UPDATE WatchlistItem SET notifiedAt = :date WHERE movieId IN (:ids)")
    abstract suspend fun markNotified(ids: List<Int>, date: LocalDate)

    @Transaction
    open suspend fun toggle(movieId: Int) {
        if (onWatchlistDirect(movieId)) {
            removeFromWatchList(movieId)
        } else {
            insertIgnore(WatchlistItem(movieId, null))
        }
    }
}
