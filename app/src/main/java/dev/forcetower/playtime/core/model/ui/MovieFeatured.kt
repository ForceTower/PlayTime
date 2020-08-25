package dev.forcetower.playtime.core.model.ui

data class MovieFeatured(
    val id: Int,
    val name: String,
    val releaseDate: String?,
    val posterUrl: String,
    val backgroundUrl: String,
    val resume: String
)