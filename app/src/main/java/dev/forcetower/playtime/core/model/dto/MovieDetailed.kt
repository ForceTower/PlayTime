package dev.forcetower.playtime.core.model.dto

import dev.forcetower.playtime.core.model.storage.Genre
import java.time.LocalDate

data class MovieDetailed(
    val id: Int,
    val title: String,
    val overview: String,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
    val posterPath: String?,
    val adult: Boolean,
    val backdropPath: String?,
    val voteAverage: Double,
    val genres: List<Genre>,
    val releaseDate: LocalDate,
    val status: String,
    val tagline: String,
    val videos: Results<Video>
)