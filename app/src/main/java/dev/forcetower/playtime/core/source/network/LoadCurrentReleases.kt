package dev.forcetower.playtime.core.source.network

import dev.forcetower.playtime.core.model.dto.values.MovieSimple
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LoadCurrentReleases {
    suspend fun execute(service: TMDbService): List<MovieSimple> {
        val results = mutableListOf<MovieSimple>()
        val current = LocalDate.now().withDayOfMonth(1)
        val start = current.minusMonths(1).withDayOfMonth(1)
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = current.plusMonths(2).withDayOfMonth(1)
            .minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)

        var completed: Boolean
        var page = 1

        do {
            try {
                val response = service.moviesByRelease(page, start, end)
                results += response.results
                completed = page >= response.totalPages
                page++
            } catch (error: IOException) {
                Timber.d(error, "Error during fetch")
                completed = true
            } catch (error: HttpException) {
                Timber.d(error, "Error during fetch")
                completed = true
            }
        } while (!completed)

        return results
    }
}