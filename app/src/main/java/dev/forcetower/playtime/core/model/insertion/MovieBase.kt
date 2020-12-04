package dev.forcetower.playtime.core.model.insertion

import dev.forcetower.playtime.core.model.dto.values.MovieSimple
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
    val releaseDate: LocalDate?
) {
    companion object {
        fun fromDTO(dto: MovieSimple): MovieBase {
            return MovieBase(
                dto.id,
                dto.title,
                dto.overview,
                dto.posterPath,
                dto.backdropPath,
                dto.video,
                dto.adult,
                dto.voteAverage,
                dto.releaseDate
            )
        }
    }
}