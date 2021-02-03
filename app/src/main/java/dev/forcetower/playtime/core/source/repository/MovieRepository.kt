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
import dev.forcetower.playtime.core.source.mediator.MovieReleaseRemoteMediator
import dev.forcetower.playtime.core.source.mediator.MoviePopularRemoteMediator
import dev.forcetower.playtime.core.source.network.MovieQuerySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun movies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { database.movies().getMovieSource() },
            remoteMediator = MoviePopularRemoteMediator(database, service)
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    fun releases(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { database.movies().getMovieReleaseSource() },
            remoteMediator = MovieReleaseRemoteMediator(database, service)
        ).flow
    }

    fun search(
        queryProvider: () -> String
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MovieQuerySource(queryProvider, database, service) }
        ).flow
    }

    fun movie(id: Int) = liveData(Dispatchers.IO) {
        emitSource(database.movies().getByIdWithRelations(id))
        try {
            val response = service.movieDetails(id)
            val genres = response.genres.map { it.asGenre() }
            val associations = response.genres.map { MovieGenre(response.id, it.id) }
            val videos = response.videos.results.map { it.asMovieVideo(response.id) }.toMutableList()
            val cast = response.credits.cast.map { it.asCast(response.id) }
            val releases = response.releaseDates.results.flatMap { it.mapToReleases(response.id) }
            val backdrops = response.images.backdrops.map { it.asBackdrop(response.id) }

            if (!videos.any { it.site.equals("YouTube", true) }) {
                videos += service.movieVideos(id, "en-US").results.map { it.asMovieVideo(id) }
            }

            database.withTransaction {
                database.releases().deleteAllFromMovie(response.id)
                database.images.deleteAllFromMovie(response.id)

                database.genres().insertOrUpdate(genres)
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
}