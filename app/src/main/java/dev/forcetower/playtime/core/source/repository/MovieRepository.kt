package dev.forcetower.playtime.core.source.repository

import android.content.SharedPreferences
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.forcetower.playtime.core.model.dto.response.WatchProvidersResponse
import dev.forcetower.playtime.core.model.dto.values.MovieDetailed
import dev.forcetower.playtime.core.model.insertion.MovieBase
import dev.forcetower.playtime.core.model.storage.Genre
import dev.forcetower.playtime.core.model.storage.Image
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.model.storage.MovieGenre
import dev.forcetower.playtime.core.model.storage.MovieReleaseFeedIndex
import dev.forcetower.playtime.core.model.storage.MovieWatchProvider
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.storage.Video
import dev.forcetower.playtime.core.model.storage.WatchProvider
import dev.forcetower.playtime.core.model.ui.ReleaseDayIndexed
import dev.forcetower.playtime.core.model.ui.ReleasesUI
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.mediator.MoviePopularRemoteMediator
import dev.forcetower.playtime.core.source.mediator.MovieRecommendationRemoteMediator
import dev.forcetower.playtime.core.source.network.LoadCurrentReleases
import dev.forcetower.playtime.core.source.network.MovieQuerySource
import dev.forcetower.playtime.core.source.network.TMDbService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.time.LocalDate
import java.util.Locale
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

    @OptIn(ExperimentalPagingApi::class)
    fun recommendations(movieId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { database.recommendations.getRecommendationsForMovie(movieId) },
            remoteMediator = MovieRecommendationRemoteMediator(movieId, database, service)
        ).flow
    }

    fun releases(): Flow<ReleasesUI> {
        val current = LocalDate.now().withDayOfMonth(1)
        val start = current.minusMonths(1).withDayOfMonth(1).toEpochDay()
        val end = current.plusMonths(2).withDayOfMonth(1).minusDays(1).toEpochDay()

        return database.movies().getReleasesBetween(start, end).map { movies ->
            ReleasesUI(
                movies = movies,
                indexer = ReleaseDayIndexed.from(movies),
                currentMovieReleaseIndex = findCurrentReleaseIndex(movies)
            )
        }
    }

    private fun findCurrentReleaseIndex(movies: List<Movie>): Int {
        val today = LocalDate.now()
        return movies.indexOfFirst { today.isEqual(it.releaseDate) || today.isBefore(it.releaseDate) }
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
        emitSource(database.movies().getById(id))
        try {
            val response = service.movieDetails(id)
            saveMovieDetails(response, database, service)

            val providers = service.movieWatchProviders(id)
            saveProviders(providers)
        } catch (error: HttpException) {
            Timber.d(error, "Error during details")
        } catch (error: IOException) {
            Timber.d(error, "Error during details")
        }
    }

    private suspend fun saveProviders(response: WatchProvidersResponse) {
        val movieId = response.id
        val results = response.results
//        val local = results[Locale.getDefault().country]
        val providersAcc = mutableListOf<WatchProvider>()
        val associations = results.entries.flatMap { entry ->
            val p1 = entry.value.flatrate?.map {
                providersAcc.add(it.asWatchProvider())
                MovieWatchProvider(movieId, it.providerId, MovieWatchProvider.TYPE_FLATRATE, entry.key)
            } ?: emptyList()
            val p2 = entry.value.buy?.map {
                providersAcc.add(it.asWatchProvider())
                MovieWatchProvider(movieId, it.providerId, MovieWatchProvider.TYPE_BUY, entry.key)
            } ?: emptyList()
            val p3 = entry.value.rent?.map {
                providersAcc.add(it.asWatchProvider())
                MovieWatchProvider(movieId, it.providerId, MovieWatchProvider.TYPE_RENT, entry.key)
            } ?: emptyList()
            p1 + p2 + p3
        }
        val providers = providersAcc.distinctBy { it.id }
        database.withTransaction {
            database.providers.insertAll(providers)
            database.providers.deleteAssociationsFromMovie(movieId)
            database.providers.insertAssociations(associations)
        }
    }

    fun genres(movieId: Int): Flow<List<Genre>> {
        return database.genres().getGenres(movieId)
    }

    fun images(movieId: Int): Flow<List<Image>> {
        return database.images.getTop4ImagesFromMovie(movieId, 0)
    }

    fun video(movieId: Int): Flow<Video?> {
        return database.videos().getFirstYouTubeVideo(movieId)
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

    fun providers(movieId: Int): Flow<List<WatchProvider>> {
        val locale = Locale.getDefault().country
        return database.providers.getProvidersOf(movieId, locale).map {
            it.distinctBy { wp -> wp.name }
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
        try {
            val results = LoadCurrentReleases.execute(service)
            val movies = results.map { MovieBase.fromDTO(it) }
            val associations =
                results.flatMap { simple -> simple.genreIds.map { MovieGenre(simple.id, it) } }
            val indices = results.mapIndexed { index, it ->
                MovieReleaseFeedIndex(it.id, index, it.releaseDate ?: LocalDate.now())
            }

            val genres = service.genres().genres.map { it.asGenre() }

            database.withTransaction {
                database.releaseFeedIndex.deleteIndex()
                database.genres().insertAll(genres)
                database.movies().insertOrUpdateSimple(movies)
                database.genres().insertAssociations(associations)
                database.releaseFeedIndex.insertAll(indices)
            }
        } catch (error: IOException) {
            Timber.d(error, "Error during load releases")
        } catch (error: HttpException) {
            Timber.d(error, "Error during load releases")
        }
    }

    companion object {
        suspend fun saveMovieDetails(
            response: MovieDetailed,
            database: PlayDB,
            service: TMDbService
        ) {
            val genres = response.genres.map { it.asGenre() }
            val associations = response.genres.map { MovieGenre(response.id, it.id) }
            val videos = response.videos.results.map { it.asMovieVideo(response.id) }.toMutableList()
            val cast = response.credits.cast.map { it.asCast(response.id) }
            val releases = response.releaseDates.results.flatMap { it.mapToReleases(response.id) }
            val backdrops = response.images.backdrops.map { it.asBackdrop(response.id) }

            if (!videos.any { it.site.equals("YouTube", true) }) {
                videos += service.movieVideos(response.id, "en-US").results.map { it.asMovieVideo(response.id) }
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
        }
    }
}
