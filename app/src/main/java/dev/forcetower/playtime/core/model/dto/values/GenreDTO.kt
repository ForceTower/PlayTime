package dev.forcetower.playtime.core.model.dto.values

import dev.forcetower.playtime.core.model.storage.Genre

data class GenreDTO(
    val id: Int,
    val name: String
) {
    fun asGenre() = Genre(id, name)
}