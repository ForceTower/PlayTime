package dev.forcetower.playtime.core.source.network.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val database: PlayDB,
    private val service: TMDbService
) : RemoteMediator<Int, Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state)?.page ?: 1
            LoadType.PREPEND -> {
                val p = getRemoteKeyForFirstItem(state)?.page ?: 1
                val n = p - 1
                if (n < 1) return MediatorResult.Success(endOfPaginationReached = true)
                n
            }
            LoadType.APPEND -> {
                val p = getRemoteKeyForLastItem(state)?.page ?: 1
                p + 1
            }
        }

        try {
            val response = service.moviesPopular(page)
            val endReached = response.page == response.totalPages

            database.withTransaction {
                database.movies().insertOrUpdateSimple(response.results.map { MovieBase.fromDTO(it, response.page) })
                Timber.d("Inserted ${response.results.size} movies")
            }
            return MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (error: HttpException) {
            Timber.e(error, "Error during fetch")
            return MediatorResult.Error(error)
        } catch (error: IOException) {
            Timber.e(error, "Error during fetch")
            return MediatorResult.Error(error)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): Movie? {
        return state.anchorPosition?.let { state.closestItemToPosition(it) }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): Movie? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): Movie? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
    }
}