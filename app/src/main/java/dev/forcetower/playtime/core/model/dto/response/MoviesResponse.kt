package dev.forcetower.playtime.core.model.dto.response

import dev.forcetower.playtime.core.model.dto.values.MovieSimple

data class MoviesResponse(
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<MovieSimple>
)