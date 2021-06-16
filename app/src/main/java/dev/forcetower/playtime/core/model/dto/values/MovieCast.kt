package dev.forcetower.playtime.core.model.dto.values

import dev.forcetower.playtime.core.model.storage.Cast

data class MovieCast(
    val castId: Int,
    val character: String,
    val creditId: String,
    val id: Int,
    val name: String,
    val profilePath: String?,
    val order: Int
) {
    fun asCast(movieId: Int): Cast {
        return Cast(creditId, movieId, castId, character, id, name, profilePath, order)
    }
}
