package dev.forcetower.playtime.core.model.insertion

import dev.forcetower.playtime.core.model.dto.MovieSimple
import dev.forcetower.playtime.core.model.storage.Movie
import timber.log.Timber
import java.time.LocalDate

data class MovieBase(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val video: Boolean,
    val adult: Boolean,
    val voteAverage: Double,
    val releaseDate: LocalDate?,
    // the tmdb index of the movie
    val position: Int
) {
    companion object {
        fun fromDTO(dto: MovieSimple, position: Int): MovieBase {
            return MovieBase(
                dto.id,
                dto.title,
                dto.overview,
                dto.posterPath,
                dto.backdropPath,
                dto.video,
                dto.adult,
                dto.voteAverage,
                dto.releaseDate,
                position
            )
        }

        fun MovieBase.toMovie(): Movie {
            return Movie(
                id,
                title,
                overview,
                posterPath,
                backdropPath,
                video,
                adult,
                voteAverage,
                releaseDate,
                null,
                position
            )
        }
    }
}