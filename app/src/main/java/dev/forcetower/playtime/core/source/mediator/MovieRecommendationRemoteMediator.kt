package dev.forcetower.playtime.core.source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.MovieRecommendation
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRecommendationRemoteMediator(
    private val movieId: Int,
    private val database: PlayDB,
    private val service: TMDbService
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val pageSize = state.config.pageSize
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val i = getRemoteKeyClosestToCurrentPosition(state)?.position ?: 1
                indexToPage(i, pageSize)
            }
            LoadType.PREPEND -> {
                val i = getRemoteKeyForFirstItem(state)?.position ?: 1
                val p = indexToPage(i, pageSize)
                val n = p - 1
                if (n < 1) return MediatorResult.Success(endOfPaginationReached = true)
                n
            }
            LoadType.APPEND -> {
                val i = getRemoteKeyForLastItem(state)?.position ?: 1
                val p = indexToPage(i, pageSize)
                p + 1
            }
        }

        try {
            val response = service.movieRecommendations(movieId, page)
            val endReached = response.page == response.totalPages

            val movies = response.results.map { MovieBase.fromDTO(it) }
            val associations = response.results.flatMap { simple -> simple.genreIds.map { MovieGenre(simple.id, it) } }
            val indices = response.results.mapIndexed { index, it ->
                MovieRecommendation(movieId, it.id, (response.page - 1) * pageSize + index)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.recommendations.deleteIndex()
                }

                database.movies().insertOrUpdateSimple(movies)
                database.genres().insertAssociations(associations)

                database.recommendations.insertAllIgnore(indices)
                Timber.d("Inserted ${response.results.size} recommendations. End reached $endReached")
            }
            return MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (error: HttpException) {
            Timber.d(error, "Error during fetch")
            return MediatorResult.Error(error)
        } catch (error: IOException) {
            Timber.d(error, "Error during fetch")
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): MovieRecommendation? {
        return state.anchorPosition?.let { state.closestItemToPosition(it) }?.let {
            database.recommendations.getRecommendationByMovieAndReference(movieId, it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): MovieRecommendation? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            database.recommendations.getRecommendationByMovieAndReference(movieId, it.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): MovieRecommendation? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            database.recommendations.getRecommendationByMovieAndReference(movieId, it.id)
        }
    }

    private fun indexToPage(position: Int, pageSize: Int = 20): Int {
        return (position / pageSize) + 1
    }
}
