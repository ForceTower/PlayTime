package dev.forcetower.playtime.core.model.dto.values

import dev.forcetower.playtime.core.model.dto.response.Results
import dev.forcetower.playtime.core.model.insertion.MovieComplete
import java.time.LocalDate

data class MovieDetailed(
    val id: Int,
    val title: String,
    val overview: String,
    val imdbId: String?,
    val popularity: Double,
    val voteCount: Int,
    val runtime: Int?,
    val video: Boolean,
    val posterPath: String?,
    val adult: Boolean,
    val backdropPath: String?,
    val voteAverage: Double,
    val genres: List<GenreDTO>,
    val releaseDate: LocalDate,
    val status: String?,
    val tagline: String,
    val videos: Results<MovieVideo>,
    val credits: MovieCredits,
    val releaseDates: Results<MovieRelease>,
    val images: MovieImages
) {
    fun asMovieComplete(): MovieComplete {
        return MovieComplete(
            id,
            title,
            overview,
            posterPath,
            backdropPath,
            runtime,
            video,
            adult,
            voteAverage,
            releaseDate,
            tagline,
            status,
            popularity
        )
    }
}