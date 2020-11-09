package dev.forcetower.playtime.core.model.insertion

import java.time.LocalDate

data class MovieComplete(
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
    val tagline: String?
)