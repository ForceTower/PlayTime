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
    fun getBackdropPathOrDefault(): String? {
        return backdropPath ?: posterPath
    }
    fun getPosterPathOrDefault(): String? {
        return posterPath ?: backdropPath
    }

    fun isTaglinePresent() = !tagline.isNullOrBlank()
    fun isOverviewPresent() = overview.isNotBlank()

    fun isRuntimePresent() = runtime != null && runtime > 0
    fun isVoteAveragePresent() = voteAverage != 0.0

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
