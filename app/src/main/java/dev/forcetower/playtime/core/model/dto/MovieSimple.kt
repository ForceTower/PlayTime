package dev.forcetower.playtime.core.model.dto

import java.time.LocalDate

data class MovieSimple(
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
    val genreIds: List<Int>,
    val releaseDate: LocalDate?
)