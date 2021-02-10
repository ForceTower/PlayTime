package dev.forcetower.playtime.core.model.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcetower.playtime.core.model.dto.values.MovieSimple
import java.time.LocalDate

@Entity
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val runtime: Int?,
    val video: Boolean,
    val adult: Boolean,
    val voteAverage: Double,
    val releaseDate: LocalDate?,
    val tagline: String?,
    val status: String?,
    val popularity: Double
) {
    companion object {
        fun fromDTO(dto: MovieSimple): Movie {
            return Movie(
                dto.id,
                dto.title,
                dto.overview,
                dto.posterPath,
                dto.backdropPath,
                null,
                dto.video,
                dto.adult,
                dto.voteAverage,
                dto.releaseDate,
                null,
                null,
                dto.popularity
            )
        }
    }
}