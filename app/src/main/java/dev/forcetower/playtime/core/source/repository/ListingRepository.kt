package dev.forcetower.playtime.core.source.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService
) {

    fun getSourceOfType(value: Int): Flow<PagingData<Movie>> {
        return when (value) {
            1 -> getUserWatchlistReleased()
            2 -> getWatched()
            else -> throw IllegalStateException("source type $value is not defined")
        }
    }

    fun getUserWatchlistUnreleased(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.watchlist.getWatchlistUnreleased() }
        ).flow
    }

    fun getUserWatchlistReleased(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.watchlist.getWatchListReleased() }
        ).flow
    }

    fun getWatched(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { database.watched.getWatchedList() }
        ).flow
    }

    fun countListUnreleased() = database.watchlist.getCountWatchlistUnreleased()
    fun countListReleased() = database.watchlist.getCountWatchListReleased()
    fun countListWatched() = database.watched.getCountWatchedList()

    fun watched(id: Int) = database.watched.isWatched(id)

    fun watchlist(id: Int) = database.watchlist.onWatchlist(id)

    suspend fun toggleFromWatchlist(movieId: Int) {
        database.watchlist.toggle(movieId)
    }

    suspend fun toggleWatchMark(movieId: Int) {
        database.watched.toggle(movieId)
    }

}