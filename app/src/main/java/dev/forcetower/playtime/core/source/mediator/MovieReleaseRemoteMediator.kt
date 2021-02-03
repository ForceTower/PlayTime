package dev.forcetower.playtime.core.source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.dto.values.MovieSimple
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.MovieReleaseFeedIndex
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

@OptIn(ExperimentalPagingApi::class)
class MovieReleaseRemoteMediator(
    private val database: PlayDB,
    private val service: TMDbService
) : RemoteMediator<Int, Movie>() {
    private val initialInterval = LocalDate.now()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val date = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state) ?: initialInterval
            }
            LoadType.PREPEND -> {
                val date = getRemoteKeyForFirstItem(state) ?: initialInterval
                date.minusDays(1)
            }
            LoadType.APPEND -> {
                val date = getRemoteKeyForLastItem(state) ?: initialInterval
                date.plusDays(1)
            }
        }

        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val results = mutableListOf<MovieSimple>()

        var completed: Boolean
        var completionError: Throwable? = null
        var page = 1
        do {
            try {
                val response = service.moviesByRelease(page, dateString)
                results += response.results
                completed = page >= response.totalPages
                page++
            } catch (error: Throwable) {
                Timber.e(error, "Error during fetch")
                completed = true
                completionError = error
            }
        } while (!completed)

        val genres = mutableListOf<Genre>()
        if (loadType == LoadType.REFRESH) {
            genres += service.genres().genres.map { it.asGenre() }
        }

        val movies = results.map { MovieBase.fromDTO(it) }
        val associations = results.flatMap { simple -> simple.genreIds.map { MovieGenre(simple.id, it) } }
        val indices = results.mapIndexed { index, it ->
            MovieReleaseFeedIndex(it.id, index, date)
        }

        val endReached = if (loadType != LoadType.REFRESH) {
            abs(date.until(initialInterval, ChronoUnit.YEARS)) >= 1
        } else false

        database.withTransaction {
            if (loadType == LoadType.REFRESH) {
                database.releaseFeedIndex.deleteIndex()
                database.genres().insertOrUpdate(genres)
            }

            database.movies().insertOrUpdateSimple(movies)
            database.genres().insertAssociations(associations)

            database.releaseFeedIndex.insertAllIgnore(indices)
            Timber.d("Inserted ${results.size} movies. End reached $endReached")
        }

        val error = completionError
        if (error != null) {
            return MediatorResult.Error(error)
        }

        return MediatorResult.Success(endReached)
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): LocalDate? {
        val anchor = state.anchorPosition ?: return null
        val item = state.closestItemToPosition(anchor) ?: return null
        val index = database.releaseFeedIndex.getReleaseIndexByIdDirect(item.id) ?: return null
        return index.startReleaseDate
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): LocalDate? {
        val item = state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull() ?: return null
        val index = database.releaseFeedIndex.getReleaseIndexByIdDirect(item.id) ?: return null
        return index.startReleaseDate
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): LocalDate? {
        val item = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull() ?: return null
        val index = database.releaseFeedIndex.getReleaseIndexByIdDirect(item.id) ?: return null
        return index.startReleaseDate
    }


    private fun indexToPage(position: Int, pageSize: Int = 20): Int {
        return (position / pageSize) + 1
    }
}