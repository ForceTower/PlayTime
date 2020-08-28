package dev.forcetower.playtime.core.source.repository

import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.network.TMDbService
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.mediator.MovieRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService
) {
    fun movies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { database.movies().getMovieSource() },
            remoteMediator = MovieRemoteMediator(database, service)
        ).flow
    }

    fun movie(id: Int) = liveData(Dispatchers.IO) {
        emitSource(database.movies().getById(id))
        try {
            val response = service.movieDetails(id)
        } catch (error: Throwable) {
            Timber.e(error, "Error during details")
        }
    }
}