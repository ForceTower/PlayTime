package dev.forcetower.playtime.core.source.repository

import androidx.lifecycle.liveData
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
                pageSize = 20
            ),
            pagingSourceFactory = { database.movies().getMovieSource() },
            remoteMediator = MovieRemoteMediator(database, service)
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

            database.withTransaction {
                database.genres().insertOrUpdate(response.genres)
                database.movies().insertOrUpdateComplete(response.asMovieComplete())
                database.genres().insertAssociations(associations)
                database.videos().insertOrUpdate(videos)
                database.cast().insertOrUpdate(cast)
                database.releases().deleteAllFromMovie(response.id)
                database.releases().insertOrUpdate(releases)
            }
        } catch (error: Throwable) {
            Timber.e(error, "Error during details")
        }
    }
}