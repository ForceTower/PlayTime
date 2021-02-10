package dev.forcetower.playtime.core.source.repository

import android.content.SharedPreferences
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed
import dev.forcetower.playtime.core.model.ui.ReleasesUI
import dev.forcetower.playtime.core.source.network.TMDbService
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.mediator.MoviePopularRemoteMediator
import dev.forcetower.playtime.core.source.network.LoadCurrentReleases
import dev.forcetower.playtime.core.source.network.MovieQuerySource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class MovieRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService,
    private val preferences: SharedPreferences
) {
    @OptIn(ExperimentalPagingApi::class)
    fun movies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { database.movies().getMovieSource() },
            remoteMediator = MoviePopularRemoteMediator(database, service)
        ).flow
    }

    fun releases(): Flow<ReleasesUI> {
        val current = LocalDate.now().withDayOfMonth(1)
        val start = current.minusMonths(1).withDayOfMonth(1).toEpochDay()
        val end = current.plusMonths(2).withDayOfMonth(1).minusDays(1).toEpochDay()

        return database.movies().getReleasesBetween(start, end).map { movies ->
            ReleasesUI(
                movies = movies,
                indexer = ReleaseDayIndexed.from(movies)
            )
        }
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

    suspend fun loadCurrentReleasesIfNeeded() = withContext(Dispatchers.IO) {
        val now = LocalDate.now()
        val cached = preferences.getLong("last_loaded_releases", 0L)
        val date = LocalDate.ofEpochDay(cached)
        if (abs(date.until(now).days) > 2 || date.monthValue != now.monthValue) {
            loadCurrentReleases()
            preferences.edit().putLong("last_loaded_releases", now.toEpochDay()).apply()
        }
    }

    suspend fun loadCurrentReleases() = withContext(Dispatchers.IO) {
        val results = LoadCurrentReleases.execute(service)
        val movies = results.map { MovieBase.fromDTO(it) }
        val associations = results.flatMap { simple -> simple.genreIds.map { MovieGenre(simple.id, it) } }

        val genres = service.genres().genres.map { it.asGenre() }

        database.withTransaction {
            database.genres().insertAll(genres)
            database.movies().insertOrUpdateSimple(movies)
            database.genres().insertAssociations(associations)
        }
    }
}