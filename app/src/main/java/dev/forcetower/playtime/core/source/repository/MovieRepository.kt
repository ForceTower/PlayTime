package dev.forcetower.playtime.core.source.repository

import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.source.network.TMDbService
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.mediator.MovieRemoteMediator
import dev.forcetower.playtime.core.source.network.MovieQuerySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService
) {
    @ExperimentalPagingApi
    fun movies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { database.movies().getMovieSource() },
            remoteMediator = MovieRemoteMediator(database, service)
        ).flow
    }

    fun search(
        queryProvider: () -> String
    ): Flow<PagingData<Movie>> {
        val factory = { MovieQuerySource(queryProvider, database, service) }
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = factory
        ).flow
    }

    fun movie(id: Int) = liveData(Dispatchers.IO) {
        emitSource(database.movies().getByIdWithGenres(id))
        try {
            val response = service.movieDetails(id)
            val associations = response.genres.map { MovieGenre(response.id, it.id) }
            val videos = response.videos.results.map { it.asMovieVideo(response.id) }
            val cast = response.credits.cast.map { it.asCast(response.id) }
            val releases = response.releaseDates.results.flatMap { it.mapToReleases(response.id) }
            val backdrops = response.images.backdrops.map { it.asBackdrop(response.id) }

            database.withTransaction {
                database.releases().deleteAllFromMovie(response.id)
                database.images.deleteAllFromMovie(response.id)

                database.genres().insertOrUpdate(response.genres)
                database.movies().insertOrUpdateComplete(response.asMovieComplete())
                database.genres().insertAssociations(associations)
                database.videos().insertOrUpdate(videos)
                database.cast().insertOrUpdate(cast)
                database.releases().insertOrUpdate(releases)
                database.images.insertOrUpdate(backdrops)
            }
        } catch (error: Throwable) {
            Timber.e(error, "Error during details")
        }
    }

    fun releaseDate(movieId: Int): Flow<Release?> {
        return database.releases()
            .getReleaseDates(movieId)
            .map { values ->
                val (digital, theater) = values.partition { it.type >= 4 }
                when {
                    digital.isNotEmpty() -> digital.minByOrNull { it.releaseDate }
                    theater.isNotEmpty() -> theater.minByOrNull { it.releaseDate }
                    else -> null
                }
            }
    }

    fun interface What {
        fun onDone()
    }
}