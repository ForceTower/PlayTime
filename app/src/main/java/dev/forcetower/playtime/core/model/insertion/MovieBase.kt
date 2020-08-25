package dev.forcetower.playtime.core.model.insertion

import dev.forcetower.playtime.core.model.dto.MovieSimple
import dev.forcetower.playtime.core.model.storage.Movie
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
    // this is a keep state of page
    val page: Int
) {
    companion object {
        fun fromDTO(dto: MovieSimple, page: Int): MovieBase {
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
                page
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
                page
            )
        }
    }
}