package dev.forcetower.playtime.core.source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val database: PlayDB,
    private val service: TMDbService
) : RemoteMediator<Int, Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val pageSize = state.config.pageSize
        Timber.d("load() $loadType")
        val page = when(loadType) {
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
        
        Timber.d("Will fetch page: $page")

        try {
            val response = service.moviesPopular(page)
            val endReached = response.page == response.totalPages

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.movies().deleteAll()
                    database.genres().insertOrUpdate(service.genres().genres)
                }
                database.movies().insertOrUpdateSimple(response.results.mapIndexed { index, it ->
                    MovieBase.fromDTO(it, (response.page - 1) * pageSize + index)
                })
                val associations = response.results.flatMap { simple -> simple.genreIds.map { MovieGenre(simple.id, it) } }
                database.genres().insertAssociations(associations)
                Timber.d("Inserted ${response.results.size} movies")
            }
            Timber.d("End reached? $endReached")
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

    private fun indexToPage(position: Int, pageSize: Int = 20): Int {
        return (position / pageSize) + 1
    }
}