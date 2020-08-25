package dev.forcetower.playtime.core.model.dto

data class MoviesResponse(
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<MovieSimple>
)