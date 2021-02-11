package dev.forcetower.playtime.core.source.repository

import android.content.Context
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcetower.playtime.core.notification.LocalNotifications
import dev.forcetower.playtime.core.source.local.PlayDB
import dev.forcetower.playtime.core.source.network.TMDbService
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseRepository @Inject constructor(
    private val database: PlayDB,
    private val service: TMDbService,
    @ApplicationContext private val context: Context
) {

    suspend fun releaseUpdater() {
        try {
            val pending = database.watchlist.getPendingMovieIdNotifications()
            val updated = pending.map { service.movieDetails(it) }
            updated.forEach { MovieRepository.saveMovieDetails(it, database, service) }
            database.withTransaction {
                val data = database.watchlist.getPendingMovieNotifications()
                data.forEach { element ->
                    val target = element.earlierRelease()
                    target?.let {
                        database.watchlist.updateTargetDate(
                            element.movie.id,
                            it.releaseDate.toLocalDate()
                        )
                    }
                }
            }
            val today = LocalDate.now()

            val values = database.watchlist.getCurrentPendingNotifications(today)
            val data = values.filter {
                val target = it.watchlistItem.targetDate ?: return@filter false
                val added = it.watchlistItem.addedAt.toLocalDate()
                target.isAfter(added)
            }
            if (data.isNotEmpty()) {
                val count = data.size
                val selected = data.map { it.movie }.shuffled().minByOrNull {
                    when {
                        it.backdropPath != null -> 1
                        it.posterPath != null -> 2
                        else -> 3
                    }
                }!!
                LocalNotifications.notificationForMovie(selected, count, context)
            }

            database.watchlist.markNotified(values.map { it.movie.id }, today)
        } catch (error: HttpException) {
            Timber.d(error, "Load failed")
        } catch (error: IOException) {
            Timber.d(error, "Load failed")
        }
    }
}